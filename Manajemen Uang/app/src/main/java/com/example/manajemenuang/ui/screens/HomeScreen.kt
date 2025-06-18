package com.example.manajemenuang.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.manajemenuang.navigation.NavDestinations
import com.example.manajemenuang.ui.components.BottomNavigationBar
import com.example.manajemenuang.ui.components.GradientBackground
import com.example.manajemenuang.ui.components.SummaryCard
import com.example.manajemenuang.ui.theme.TextWhite
import com.example.manajemenuang.ui.theme.getAccentColor
import com.example.manajemenuang.viewmodel.TransactionViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext

// Layar beranda aplikasi Manajemen Uang
// Mengoptimalkan performa dengan derived state
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(LocalContext.current as ComponentActivity)
) {
    // Mendapatkan data transaksi dari ViewModel
    val transactions by viewModel.transactions.collectAsState()
    
    // Menghitung ringkasan keuangan menggunakan derived state
    val financialData by remember(transactions) {
        derivedStateOf {
            Triple(
                viewModel.getTotalIncome(),
                viewModel.getTotalExpense(),
                viewModel.getBalance()
            )
        }
    }
    
    // Menggunakan derived state untuk menentukan apakah list kosong
    val isEmpty by remember(transactions) {
        derivedStateOf { transactions.isEmpty() }
    }
    
    // Mendapatkan warna aksen berbasis tema untuk FAB
    val accentColor = getAccentColor()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavDestinations.ADD_TRANSACTION_SCREEN)
                },
                containerColor = accentColor
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Transaksi",
                    tint = TextWhite
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
                    .padding(16.dp)
            ) {
                // Judul layar
                Text(
                    text = "Manajemen Uang",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Komponen kartu ringkasan
                SummaryCard(
                    income = financialData.first,
                    expense = financialData.second,
                    balance = financialData.third
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Menampilkan status kosong jika tidak ada transaksi
                if (isEmpty) {
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
                }
            }
        }
    }
} 