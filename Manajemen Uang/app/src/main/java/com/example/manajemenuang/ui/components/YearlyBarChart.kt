package com.example.manajemenuang.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manajemenuang.ui.theme.ExpenseRed
import com.example.manajemenuang.ui.theme.IncomeGreen
import com.example.manajemenuang.utils.formatAsRupiah

// Komponen untuk menampilkan perbandingan pemasukan dan pengeluaran tahunan sebagai diagram batang.
//
// @param totalIncome Total jumlah pemasukan untuk tahun tersebut
// @param totalExpense Total jumlah pengeluaran untuk tahun tersebut
// @param year Tahun yang sedang ditampilkan
// @param modifier Modifier opsional untuk menyesuaikan komponen
@Composable
fun YearlyBarChart(
    totalIncome: Double,
    totalExpense: Double,
    year: String,
    modifier: Modifier = Modifier
) {
    val maxValue = maxOf(totalIncome, totalExpense, 1.0) // Memastikan kita tidak membagi dengan nol
    val incomeBarHeight = (totalIncome / maxValue * 200).toInt().coerceAtLeast(4).dp
    val expenseBarHeight = (totalExpense / maxValue * 200).toInt().coerceAtLeast(4).dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perbandingan Pemasukan & Pengeluaran $year",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Diagram batang
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Diagram Batang untuk Pemasukan
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatAsRupiah(totalIncome),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(incomeBarHeight)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(IncomeGreen.copy(alpha = 0.8f))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pemasukan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Diagram Batang untuk Pengeluaran
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatAsRupiah(totalExpense),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(expenseBarHeight)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(ExpenseRed.copy(alpha = 0.8f))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pengeluaran",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
} 