package com.example.manajemenuang.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.manajemenuang.data.model.CategoryList
import com.example.manajemenuang.data.model.Transaction
import com.example.manajemenuang.data.model.TransactionType
import com.example.manajemenuang.ui.components.GradientBackground
import com.example.manajemenuang.ui.theme.ButtonShape
import com.example.manajemenuang.ui.theme.ExpenseRed
import com.example.manajemenuang.ui.theme.IncomeGreen
import com.example.manajemenuang.ui.theme.InputFieldShape
import com.example.manajemenuang.ui.theme.PrimaryTeal
import com.example.manajemenuang.ui.theme.SurfaceLight
import com.example.manajemenuang.ui.theme.TextWhite
import com.example.manajemenuang.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext

// Layar untuk mengedit transaksi yang sudah ada
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    transactionId: String,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(LocalContext.current as ComponentActivity)
) {
    // Mendapatkan semua transaksi
    val transactions by viewModel.transactions.collectAsState()
    
    // Mencari transaksi yang akan diedit
    val transaction = transactions.find { it.id == transactionId }
    
    // Jika transaksi tidak ditemukan, navigasi kembali
    if (transaction == null) {
        LaunchedEffect(Unit) {
            navController.navigateUp()
        }
        return
    }
    
    // Field-field transaksi dengan nilai awal dari transaksi
    var amount by rememberSaveable { mutableStateOf(transaction.amount.toString()) }
    var title by rememberSaveable { mutableStateOf(transaction.title) }
    var category by rememberSaveable { mutableStateOf(transaction.category) }
    var description by rememberSaveable { mutableStateOf(transaction.description) }
    var transactionType by rememberSaveable { mutableStateOf(transaction.type) }
    
    // Status pemilih tanggal
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Tanggal yang dipilih
    var selectedDate by remember { mutableStateOf(transaction.date) }
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale("id", "ID"))
    var formattedDate by remember { mutableStateOf(dateFormat.format(selectedDate)) }
    
    // Dialog pemilih tanggal
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time,
            initialDisplayMode = DisplayMode.Picker
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Date(millis)
                            formattedDate = dateFormat.format(selectedDate)
                        }
                        showDatePicker = false
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("OK", fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Batal", fontSize = 14.sp)
                }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pilih Tanggal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = dateFormat.format(datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
        }
    }
    
    // Dropdown metode pembayaran
    var expanded by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf(transaction.paymentMethod) }
    val paymentMethods = listOf("Uang Tunai", "Kartu Kredit", "Kartu Debit", "E-Wallet", "Transfer Bank")
    
    // Menyiapkan kategori berdasarkan jenis transaksi
    val categories = if (transactionType == TransactionType.EXPENSE) {
        CategoryList.expenseCategories
    } else {
        CategoryList.incomeCategories
    }
    
    // Judul cepat berdasarkan jenis transaksi
    val quickTitles = if (transactionType == TransactionType.EXPENSE) 
        listOf("Makanan", "Listrik", "Belanja", "Jajan", "Bensin", "Pulsa") 
    else 
        listOf("Gaji", "Bonus", "Hadiah", "Penjualan", "Investasi", "Lainnya")
    
    // Tambahkan state untuk pesan error/sukses
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var shouldNavigateUp by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaksi", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryTeal
                )
            )
        }
    ) { paddingValues ->
        GradientBackground(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Form content in a card
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceLight
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Transaction type selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Expense button
                            Button(
                                onClick = { transactionType = TransactionType.EXPENSE },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (transactionType == TransactionType.EXPENSE) ExpenseRed else Color.Gray.copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Pengeluaran")
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Income button
                            Button(
                                onClick = { transactionType = TransactionType.INCOME },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (transactionType == TransactionType.INCOME) IncomeGreen else Color.Gray.copy(alpha = 0.5f)
                                )
                            ) {
                                Text("Pemasukan")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Date and time row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Date selector
                            OutlinedTextField(
                                value = formattedDate,
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                label = { Text("Tanggal") },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            imageVector = Icons.Filled.DateRange,
                                            contentDescription = "Pilih Tanggal"
                                        )
                                    }
                                },
                                shape = InputFieldShape
                            )
                            
                            // Time is static for simplicity
                            OutlinedTextField(
                                value = "22:58",
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier
                                    .width(120.dp),
                                label = { Text("Waktu") },
                                shape = InputFieldShape
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Payment method dropdown
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedPaymentMethod,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = InputFieldShape,
                                label = { Text("Metode Pembayaran") }
                            )
                            
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                paymentMethods.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedPaymentMethod = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Amount input
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Jumlah") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Calculator"
                                )
                            },
                            shape = InputFieldShape
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Title input
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Judul Transaksi") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputFieldShape
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Quick title buttons
                        Text("Judul Cepat:", 
                            fontSize = 14.sp, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            quickTitles.forEach { quickTitle ->
                                QuickSelectButton(
                                    text = quickTitle,
                                    onClick = { title = quickTitle }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Category input
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Kategori") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputFieldShape
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Quick category buttons
                        Text("Kategori Cepat:", 
                            fontSize = 14.sp, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { categoryName ->
                                QuickSelectButton(
                                    text = categoryName,
                                    onClick = { category = categoryName }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Description input
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Deskripsi (opsional)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = InputFieldShape
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Save button
                        Button(
                            onClick = {
                                errorMessage = ""
                                successMessage = ""
                                if (amount.isNotBlank() && title.isNotBlank()) {
                                    val amountValue = amount.toDoubleOrNull()
                                    if (amountValue != null) {
                                        viewModel.editTransaction(
                                            transaction = transaction,
                                            amount = amountValue,
                                            title = title,
                                            category = category.ifBlank { title },
                                            type = transactionType,
                                            paymentMethod = selectedPaymentMethod,
                                            description = description,
                                            date = selectedDate
                                        )
                                        successMessage = "Perubahan berhasil disimpan!"
                                        shouldNavigateUp = true
                                    } else {
                                        errorMessage = "Jumlah harus berupa angka yang valid."
                                    }
                                } else {
                                    errorMessage = "Jumlah dan judul tidak boleh kosong."
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = ButtonShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (transactionType == TransactionType.EXPENSE) ExpenseRed else IncomeGreen
                            )
                        ) {
                            Text("Simpan Perubahan", fontSize = 16.sp)
                        }
                        if (errorMessage.isNotBlank()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        if (successMessage.isNotBlank()) {
                            Text(
                                text = successMessage,
                                color = Color(0xFF388E3C),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        // Navigasi setelah sukses
                        if (shouldNavigateUp) {
                            LaunchedEffect(shouldNavigateUp) {
                                kotlinx.coroutines.delay(500)
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
} 