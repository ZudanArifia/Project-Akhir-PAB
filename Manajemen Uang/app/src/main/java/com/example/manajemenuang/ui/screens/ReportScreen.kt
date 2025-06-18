package com.example.manajemenuang.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.manajemenuang.data.model.Transaction
import com.example.manajemenuang.data.model.TransactionType
import com.example.manajemenuang.ui.components.BottomNavigationBar
import com.example.manajemenuang.ui.components.GradientBackground
import com.example.manajemenuang.ui.components.TransactionCard
import com.example.manajemenuang.ui.components.YearlyBarChart
import com.example.manajemenuang.ui.components.YearSelector
import com.example.manajemenuang.ui.components.MonthYearSelector
import com.example.manajemenuang.ui.theme.ExpenseRed
import com.example.manajemenuang.ui.theme.IncomeGreen
import com.example.manajemenuang.ui.theme.TextWhite
import com.example.manajemenuang.utils.formatAsRupiah
import com.example.manajemenuang.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import kotlinx.coroutines.delay
import com.example.manajemenuang.ui.theme.getNavigationColor
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.update
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(LocalContext.current as ComponentActivity)
) {
    // Mendapatkan semua transaksi dengan collectAsState
    val transactions by viewModel.transactions.collectAsState()
    
    // Mendapatkan total pendapatan, pengeluaran, dan saldo dari ViewModel
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val balance by viewModel.balance.collectAsState()
    
    // Status untuk tab yang dipilih
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    // Mendapatkan tanggal saat ini
    val calendar = Calendar.getInstance()
    val currentDate = calendar.time
    
    // Status untuk tanggal bulan/tahun yang dipilih
    var selectedMonthYearDate by rememberSaveable { mutableStateOf(currentDate) }
    
    // Menghitung tanggal awal dan akhir untuk bulan yang dipilih
    val startOfSelectedMonth = Calendar.getInstance().apply {
        time = selectedMonthYearDate
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
    
    val endOfSelectedMonth = Calendar.getInstance().apply {
        time = selectedMonthYearDate
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
    
    // Menghitung tanggal awal dan akhir untuk bulan saat ini (untuk tab realtime)
    val startOfMonth = Calendar.getInstance().apply {
        time = currentDate
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
    
    val endOfMonth = Calendar.getInstance().apply {
        time = currentDate
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
    
    // Format tanggal untuk ditampilkan
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
    val yearFormat = SimpleDateFormat("yyyy", Locale("id", "ID"))
    
    // Mendapatkan string bulan dan tahun saat ini
    val currentMonthYear = monthYearFormat.format(currentDate)
    val currentYear = yearFormat.format(currentDate)
    val selectedMonthYear = monthYearFormat.format(selectedMonthYearDate)
    
    // Menghitung tanggal awal dan akhir untuk tahun saat ini
    val startOfYear = Calendar.getInstance().apply {
        time = currentDate
        set(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val endOfYear = Calendar.getInstance().apply {
        time = currentDate
        set(Calendar.DAY_OF_YEAR, getActualMaximum(Calendar.DAY_OF_YEAR))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time

    // Menghitung total untuk realtime
    val monthlyTotalIncome = viewModel.getTotalIncome(startOfMonth, endOfMonth)
    val monthlyTotalExpense = viewModel.getTotalExpense(startOfMonth, endOfMonth)
    val monthlyBalance = viewModel.getBalance(startOfMonth, endOfMonth)
    
    // Menghitung total untuk bulan yang dipilih
    val selectedMonthTotalIncome = viewModel.getTotalIncome(startOfSelectedMonth, endOfSelectedMonth)
    val selectedMonthTotalExpense = viewModel.getTotalExpense(startOfSelectedMonth, endOfSelectedMonth)
    val selectedMonthBalance = viewModel.getBalance(startOfSelectedMonth, endOfSelectedMonth)
    
    // Menghitung total untuk tahunan
    val yearlyTotalIncome = viewModel.getTotalIncome(startOfYear, endOfYear)
    val yearlyTotalExpense = viewModel.getTotalExpense(startOfYear, endOfYear)
    val yearlyBalance = viewModel.getBalance(startOfYear, endOfYear)
    
    // Mendapatkan transaksi yang dikelompokkan berdasarkan kategori untuk bulan yang dipilih
    val selectedMonthExpensesByCategory = viewModel.getTransactionsByCategory(
        startDate = startOfSelectedMonth,
        endDate = endOfSelectedMonth,
            type = TransactionType.EXPENSE
        )
    
    // LaunchedEffect kunci untuk memastikan data laporan selalu diperbarui ketika transaksi berubah
    LaunchedEffect(transactions, selectedTabIndex, selectedMonthYearDate) {
        // Memaksa ViewModel untuk memperbarui semua perhitungan
        viewModel.updateTotals()
        viewModel.updateFilteredTransactions()
    }
    
    // Mendapatkan warna navigasi yang sesuai tema
    val navigationColor = getNavigationColor()
    
    // Menyediakan NavController ke composable melalui CompositionLocal
    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
            GradientBackground(
                modifier = Modifier.padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Judul
                    Text(
                        text = "Laporan",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    
                    // Baris Tab
                    val tabTitles = listOf("Realtime", "Bulanan", "Tahunan")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .background(navigationColor, RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val isSelected = selectedTabIndex == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) Color.White else navigationColor
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) navigationColor else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable { selectedTabIndex = index }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) navigationColor else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (index < tabTitles.lastIndex) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Konten berdasarkan tab yang dipilih
                    when (selectedTabIndex) {
                        0 -> RealtimeReportContent(
                            totalIncome = totalIncome,
                            totalExpense = totalExpense,
                            balance = balance,
                            transactions = transactions
                        )
                        1 -> MonthlyReportContent(
                            selectedDate = selectedMonthYearDate,
                            onDateSelected = { selectedMonthYearDate = it },
                            totalIncome = selectedMonthTotalIncome,
                            totalExpense = selectedMonthTotalExpense,
                            balance = selectedMonthBalance,
                            expensesByCategory = selectedMonthExpensesByCategory
                        )
                        2 -> YearlyReportContent(
                            year = currentYear,
                            totalIncome = yearlyTotalIncome,
                            totalExpense = yearlyTotalExpense,
                            balance = yearlyBalance,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RealtimeReportContent(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    transactions: List<Transaction>
) {
    // Mendapatkan ViewModel
    val viewModel: TransactionViewModel = viewModel(LocalContext.current as ComponentActivity)
    // Controller navigasi
    val navController = LocalNavController.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Ringkasan Keuangan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Income row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(IncomeGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "Pemasukan",
                                tint = IncomeGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Pemasukan",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Text(
                        text = "+ ${formatAsRupiah(totalIncome)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = IncomeGreen
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Expense row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ExpenseRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = "Pengeluaran",
                                tint = ExpenseRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Pengeluaran",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Text(
                        text = "- ${formatAsRupiah(totalExpense)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ExpenseRed
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Balance row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saldo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = formatAsRupiah(balance),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (balance >= 0) IncomeGreen else ExpenseRed
                    )
                }
            }
        }
        
        // Transactions grouped by date
        Text(
            text = "Transaksi",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        if (transactions.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada transaksi",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            // Group transactions by date
            val groupedTransactions = transactions
                .sortedByDescending { it.date } // Sort by date descending (newest first)
                .groupBy { 
                    // Group by date (without time)
                    val calendar = Calendar.getInstance()
                    calendar.time = it.date
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.time
                }
            
            // Display transactions grouped by date
            groupedTransactions.forEach { (date, transactionsForDate) ->
                TransactionDateGroup(
                    date = date,
                    transactions = transactionsForDate.sortedByDescending { it.date }, // Sort by time within date
                    modifier = Modifier.padding(vertical = 4.dp),
                    onEditClick = { transaction ->
                        // Navigate to edit screen with transaction ID
                        navController.navigate("edit_transaction/${transaction.id}")
                    },
                    onDeleteClick = { transaction ->
                        // Delete the transaction
                        viewModel.deleteTransaction(transaction)
                    }
                )
            }
        }
    }
}

// Import tambahan untuk LocalNavController
val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

@Composable
fun TransactionDateGroup(
    date: Date,
    transactions: List<Transaction>,
    modifier: Modifier = Modifier,
    onEditClick: (Transaction) -> Unit = {},
    onDeleteClick: (Transaction) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Format the date as "dd MMMM yyyy (Day name)"
    val dateFormat = SimpleDateFormat("dd MMMM yyyy (EEEE)", Locale("id", "ID"))
    val formattedDate = dateFormat.format(date)
    
    // Calculate total balance for this date
    val incomeTotal = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expenseTotal = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val dateBalance = incomeTotal - expenseTotal
    
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Date header with expand/collapse arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Text(
                    text = formattedDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                
                // Total balance for the day
                Text(
                    text = formatAsRupiah(dateBalance),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dateBalance >= 0) IncomeGreen else ExpenseRed,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                // Expand/collapse button
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Sembunyikan" else "Tampilkan",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            // Show transactions if expanded
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    // Divider between header and transactions
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // List of transactions for this date
                    transactions.forEach { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            onEditClick = onEditClick,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyReportContent(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    expensesByCategory: Map<String, Double>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Month Year Selector
        MonthYearSelector(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Income column
                    Column {
                        Text(
                            text = "Pemasukan",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "+ ${formatAsRupiah(totalIncome)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IncomeGreen
                        )
                    }
                    
                    // Expense column
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Pengeluaran",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "- ${formatAsRupiah(totalExpense)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRed
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Balance row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saldo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = formatAsRupiah(balance),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (balance >= 0) IncomeGreen else ExpenseRed
                    )
                }
            }
        }
        
        // Expense by Category Chart
        if (expensesByCategory.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Pengeluaran per Kategori",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Simple pie chart using Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomPieChart(
                            data = expensesByCategory,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Legend
                    Column {
                        expensesByCategory.entries.toList().forEachIndexed { index, entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Color indicator
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            getColorForIndex(index),
                                            shape = CircleShape
                                        )
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // Category name
                                Text(
                                    text = entry.key,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                // Amount
                                Text(
                                    text = formatAsRupiah(entry.value),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearlyReportContent(
    year: String,
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    viewModel: TransactionViewModel
) {
    // State for the currently selected year
    var selectedYear by remember { mutableStateOf<String>(year) }

    // Observe transactions to trigger recomposition when data changes
    val transactions by viewModel.transactions.collectAsState()
    
    // Calculate the yearly data based on the selected year - recomputed on every recomposition
    val yearlyIncome = viewModel.getYearlyIncome(selectedYear.toInt())
    val yearlyExpense = viewModel.getYearlyExpense(selectedYear.toInt())
    val yearlyBalance = viewModel.getYearlyBalance(selectedYear.toInt())
    
    // Force update calculations when in this screen
    LaunchedEffect(selectedYear, transactions) {
        viewModel.updateTotals()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Year display
        Text(
            text = "Laporan Tahunan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Year selector
        YearSelector(
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Income column
                    Column {
                        Text(
                            text = "Total Pemasukan",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "+ ${formatAsRupiah(yearlyIncome)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IncomeGreen
                        )
                    }
                    
                    // Expense column
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Pengeluaran",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "- ${formatAsRupiah(yearlyExpense)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRed
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Balance row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saldo Tahunan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = formatAsRupiah(yearlyBalance),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (yearlyBalance >= 0) IncomeGreen else ExpenseRed
                    )
                }
            }
        }
        
        // Bar chart visualization
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            YearlyBarChart(
                totalIncome = yearlyIncome,
                totalExpense = yearlyExpense,
                year = selectedYear,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CustomPieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val total = data.values.sum()
    val colors = data.keys.mapIndexed { index, _ -> getColorForIndex(index) }
    
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = min(canvasWidth, canvasHeight) / 2f
        val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
        
        var startAngle = 0f
        
        data.values.forEachIndexed { index, value ->
            val sweepAngle = (value / total * 360f).toFloat()
            val color = colors[index]
            
            // Draw pie slice
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            // Draw outline
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Stroke(width = 2f),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            startAngle += sweepAngle
        }
    }
}

// function untuk mendapatkan warna pie chart segments
private fun getColorForIndex(index: Int): Color {
    val colors = listOf(
        ExpenseRed,
        Color(0xFF2196F3), // Blue
        Color(0xFFFFC107), // Amber
        Color(0xFF4CAF50), // Green
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF5722), // Deep Orange
        Color(0xFF607D8B)  // Blue Grey
    )
    return colors[index % colors.size]
} 