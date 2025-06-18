package com.example.manajemenuang.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenuang.data.model.Transaction
import com.example.manajemenuang.data.model.TransactionType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.Calendar
import java.util.Date

// ViewModel untuk mengelola data transaksi
// Menggunakan SharedPreferences untuk menyimpan data secara lokal
class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "transaction_prefs", Context.MODE_PRIVATE
    )
    private val gson = Gson()

    // State untuk menyimpan semua transaksi
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // State untuk menyimpan transaksi yang difilter
    private val _filteredTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val filteredTransactions: StateFlow<List<Transaction>> = _filteredTransactions.asStateFlow()

    // State untuk menyimpan total pendapatan, pengeluaran, dan saldo
    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    init {
        viewModelScope.launch {
            loadTransactions()
            updateTotals()
        }
    }

    // Menambahkan transaksi baru
    fun addTransaction(
        amount: Double,
        title: String,
        category: String,
        type: TransactionType,
        paymentMethod: String,
        description: String,
        date: Date = Date()
    ) {
        viewModelScope.launch {
            val newTransaction = Transaction(
                amount = amount,
                title = title,
                category = category,
                type = type,
                paymentMethod = paymentMethod,
                description = description,
                date = date
            )
            
            // Memperbarui menggunakan update() untuk emisi yang tepat
            _transactions.update { currentList ->
                val updatedList = currentList.toMutableList()
                updatedList.add(0, newTransaction)
                updatedList
            }
            
            // Memperbarui transaksi yang difilter
            updateFilteredTransactions()
            
            // Menyimpan ke SharedPreferences
            saveTransactions()
            
            // Memperbarui total
            updateTotals()
        }
    }
    
    // Mengedit transaksi yang sudah ada
    fun editTransaction(
        transaction: Transaction,
        amount: Double,
        title: String,
        category: String,
        type: TransactionType,
        paymentMethod: String,
        description: String,
        date: Date
    ) {
        viewModelScope.launch {
            // Gunakan update() untuk pembaruan state atomik dengan emisi yang tepat
            _transactions.update { currentList ->
                val mutableList = currentList.toMutableList()
                val index = mutableList.indexOfFirst { it.id == transaction.id }
                
                if (index != -1) {
                    // Membuat transaksi yang diperbarui dengan ID yang sama
                    val updatedTransaction = Transaction(
                        id = transaction.id,
                        amount = amount,
                        title = title,
                        category = category,
                        type = type,
                        paymentMethod = paymentMethod,
                        description = description,
                        date = date
                    )
                    
                    // Mengganti transaksi lama dengan yang baru diperbarui
                    mutableList[index] = updatedTransaction
                }
                mutableList
            }
            
            // Menyimpan ke SharedPreferences
            saveTransactions()
            
            // Memperbarui transaksi yang difilter
            updateFilteredTransactions()
            
            // Memperbarui total
            updateTotals()
        }
    }
    
    // Menghapus transaksi
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // Gunakan update() untuk pembaruan state atomik dengan emisi yang tepat
            _transactions.update { currentList ->
                currentList.filter { it.id != transaction.id }
            }
            
            // Menyimpan ke SharedPreferences
            saveTransactions()
            
            // Memperbarui transaksi yang difilter
            updateFilteredTransactions()
            
            // Memperbarui total
            updateTotals()
        }
    }
    
    // Memperbarui total pendapatan, pengeluaran, dan saldo
    fun updateTotals() {
        viewModelScope.launch {
            val currentTransactions = _transactions.value
            
            val totalIncome = currentTransactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            
            val totalExpense = currentTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
            
            val balance = totalIncome - totalExpense
            
            // Gunakan update() untuk emisi yang tepat
            _totalIncome.update { totalIncome }
            _totalExpense.update { totalExpense }
            _balance.update { balance }
        }
    }
    
    // Mendapatkan total jumlah pendapatan
    fun getTotalIncome(): Double {
        return _transactions.value
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }
    
    // Mendapatkan total jumlah pengeluaran
    fun getTotalExpense(): Double {
        return _transactions.value
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }
    
    // Mendapatkan saldo bersih (pendapatan - pengeluaran)
    fun getBalance(): Double {
        return getTotalIncome() - getTotalExpense()
    }
    
    // Mendapatkan total jumlah pendapatan untuk rentang tanggal tertentu
    fun getTotalIncome(startDate: Date, endDate: Date): Double {
        return _transactions.value
            .filter { 
                it.type == TransactionType.INCOME && 
                it.date.time >= startDate.time && 
                it.date.time <= endDate.time 
            }
            .sumOf { it.amount }
    }

    // Mendapatkan total jumlah pengeluaran untuk rentang tanggal tertentu
    fun getTotalExpense(startDate: Date, endDate: Date): Double {
        return _transactions.value
            .filter { 
                it.type == TransactionType.EXPENSE && 
                it.date.time >= startDate.time && 
                it.date.time <= endDate.time 
            }
            .sumOf { it.amount }
    }

    // Mendapatkan saldo bersih (pendapatan - pengeluaran) untuk rentang tanggal tertentu
    fun getBalance(startDate: Date, endDate: Date): Double {
        return getTotalIncome(startDate, endDate) - getTotalExpense(startDate, endDate)
    }
    
    // Memfilter transaksi berdasarkan tanggal
    fun filterTransactionsByDate(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            val filtered = _transactions.value.filter { 
                it.date.time >= startDate.time && it.date.time <= endDate.time 
            }
            // Gunakan update() untuk emisi yang tepat
            _filteredTransactions.update { filtered }
        }
    }
    
    // Mendapatkan transaksi yang dikelompokkan berdasarkan kategori untuk laporan
    fun getTransactionsByCategory(startDate: Date, endDate: Date, type: TransactionType): Map<String, Double> {
        return _transactions.value
            .filter { 
                it.type == type && 
                it.date.time >= startDate.time && 
                it.date.time <= endDate.time 
            }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
    
    // Mendapatkan transaksi yang dikelompokkan berdasarkan tanggal untuk laporan
    fun getTransactionsByDate(startDate: Date, endDate: Date): Map<String, List<Transaction>> {
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("id", "ID"))
        
        return _transactions.value
            .filter { 
                it.date.time >= startDate.time && 
                it.date.time <= endDate.time 
            }
            .groupBy { dateFormat.format(it.date) }
    }
    
    // Memperbarui daftar transaksi yang difilter berdasarkan filter saat ini
    fun updateFilteredTransactions() {
        viewModelScope.launch {
            // Gunakan update() untuk emisi yang tepat
            _filteredTransactions.update { _transactions.value }
            updateTotals()
        }
    }

    // Menyimpan transaksi ke SharedPreferences
    private fun saveTransactions() {
        viewModelScope.launch {
            try {
                val json = gson.toJson(_transactions.value)
                sharedPreferences.edit().putString(TRANSACTIONS_KEY, json).commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Memuat transaksi dari SharedPreferences
    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                val json = sharedPreferences.getString(TRANSACTIONS_KEY, null)
                if (json != null) {
                    val type: Type = object : TypeToken<List<Transaction>>() {}.type
                    val loadedTransactions: List<Transaction> = gson.fromJson(json, type)
                    // Gunakan update() untuk emisi yang tepat
                    _transactions.update { loadedTransactions }
                    _filteredTransactions.update { loadedTransactions }
                    updateTotals()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _transactions.update { emptyList() }
                _filteredTransactions.update { emptyList() }
                updateTotals()
            }
        }
    }

    // Mendapatkan tanggal awal untuk tahun tertentu (1 Januari)
    fun getStartOfYear(year: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    // Mendapatkan tanggal akhir untuk tahun tertentu (31 Desember)
    fun getEndOfYear(year: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time
    }

    // Mendapatkan total pendapatan untuk tahun tertentu
    fun getYearlyIncome(year: Int): Double {
        val startDate = getStartOfYear(year)
        val endDate = getEndOfYear(year)
        return getTotalIncome(startDate, endDate)
    }

    // Mendapatkan total pengeluaran untuk tahun tertentu
    fun getYearlyExpense(year: Int): Double {
        val startDate = getStartOfYear(year)
        val endDate = getEndOfYear(year)
        return getTotalExpense(startDate, endDate)
    }

    // Mendapatkan saldo untuk tahun tertentu
    fun getYearlyBalance(year: Int): Double {
        val startDate = getStartOfYear(year)
        val endDate = getEndOfYear(year)
        return getBalance(startDate, endDate)
    }

    companion object {
        private const val TRANSACTIONS_KEY = "saved_transactions"
    }
} 