package com.example.manajemenuang.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.manajemenuang.data.model.Transaction
import com.example.manajemenuang.navigation.NavDestinations
import com.example.manajemenuang.ui.components.BottomNavigationBar
import com.example.manajemenuang.ui.components.GradientBackground
import com.example.manajemenuang.ui.components.TransactionCard
import com.example.manajemenuang.ui.theme.ExpenseRed
import com.example.manajemenuang.ui.theme.IncomeGreen
import com.example.manajemenuang.ui.theme.PrimaryTeal
import com.example.manajemenuang.ui.theme.TextWhite
import com.example.manajemenuang.viewmodel.TransactionViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext


// Layar untuk menampilkan daftar transaksi
// Menggunakan LazyColumn dengan state untuk performa yang lebih baik
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(LocalContext.current as ComponentActivity)
) {
    // Mendapatkan daftar transaksi dari ViewModel
    val transactions by viewModel.transactions.collectAsState()
    
    // Menggunakan LazyListState untuk mengoptimalkan scrolling
    val listState = rememberLazyListState()
    
    // Derived state untuk menentukan apakah list kosong
    val isEmpty by remember {
        derivedStateOf { transactions.isEmpty() }
    }
    
    // Status untuk dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            // Tombol FAB tunggal yang menavigasi ke ADD_TRANSACTION_SCREEN
            FloatingActionButton(
                onClick = { navController.navigate(NavDestinations.ADD_TRANSACTION_SCREEN) },
                containerColor = PrimaryTeal,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Transaksi",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        GradientBackground(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                // Judul layar
                Text(
                    text = "Transaksi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
                
                // Menampilkan transaksi atau status kosong
                if (isEmpty) {
                    // Status kosong
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada data transaksi.\nKlik tombol + untuk menambahkan transaksi.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = TextWhite
                        )
                    }
                } else {
                    // Daftar transaksi dengan LazyColumn yang dioptimalkan
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        state = listState
                    ) {
                        items(
                            items = transactions,
                            key = { it.id } // Menggunakan key untuk performa yang lebih baik
                        ) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                onEditClick = { 
                                    // Navigasi ke layar edit dengan ID transaksi
                                    navController.navigate(NavDestinations.EDIT_TRANSACTION_SCREEN + "/${transaction.id}")
                                },
                                onDeleteClick = { 
                                    // Tampilkan konfirmasi hapus
                                    transactionToDelete = transaction
                                    showDeleteDialog = true
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        
        // Dialog konfirmasi hapus
        if (showDeleteDialog && transactionToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus transaksi '${transactionToDelete?.title}'?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            transactionToDelete?.let { viewModel.deleteTransaction(it) }
                            showDeleteDialog = false
                            transactionToDelete = null
                        }
                    ) {
                        Text("Hapus", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
} 