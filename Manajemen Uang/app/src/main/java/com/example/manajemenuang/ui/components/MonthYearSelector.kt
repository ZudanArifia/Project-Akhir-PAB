package com.example.manajemenuang.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Komponen untuk memilih bulan dan tahun.
//
// @param selectedDate Tanggal yang dipilih saat ini (hanya bulan dan tahun yang digunakan)
// @param onDateSelected Callback ketika tanggal dipilih
// @param yearsRange Jumlah tahun yang ditampilkan sebelum dan sesudah tahun saat ini
// @param modifier Modifier opsional untuk menyesuaikan komponen
@Composable
fun MonthYearSelector(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    yearsRange: Int = 3,
    modifier: Modifier = Modifier
) {
    // Format untuk tampilan
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
    val monthFormat = SimpleDateFormat("MMMM", Locale("id", "ID"))
    
    // Status untuk menu yang diperluas
    var isExpanded by remember { mutableStateOf(false) }
    
    // Mendapatkan informasi tanggal saat ini
    val currentCal = Calendar.getInstance()
    val currentYear = currentCal.get(Calendar.YEAR)
    
    // Menghasilkan daftar tahun (tahun lalu, saat ini, dan tahun mendatang)
    val years = (-yearsRange..yearsRange).map { currentYear + it }
    
    // Menghasilkan daftar semua bulan
    val months = (0..11).map { monthIndex ->
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, monthIndex)
        Pair(monthIndex, monthFormat.format(cal.time))
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Selected month/year header (clickable to expand/collapse)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthYearFormat.format(selectedDate),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (isExpanded) "Tutup" else "Pilih Bulan dan Tahun",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Dropdown menu (animated)
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Year selector
                    Text(
                        text = "Pilih Tahun",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)
                    )
                    
                    // Show years in rows of 5
                    for (rowIndex in 0 until (years.size + 4) / 5) {
                        val startIndex = rowIndex * 5
                        val endIndex = minOf(startIndex + 5, years.size)
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            for (i in startIndex until endIndex) {
                                val year = years[i]
                                val cal = Calendar.getInstance()
                                cal.time = selectedDate
                                val isSelected = cal.get(Calendar.YEAR) == year
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        )
                                        .clickable {
                                            val newCal = Calendar.getInstance()
                                            newCal.time = selectedDate
                                            newCal.set(Calendar.YEAR, year)
                                            onDateSelected(newCal.time)
                                        }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = year.toString(),
                                        color = if (isSelected) Color.White 
                                                else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            
                            // Add empty spacers for incomplete rows
                            if (endIndex - startIndex < 5) {
                                repeat(5 - (endIndex - startIndex)) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Month selector
                    Text(
                        text = "Pilih Bulan",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    )
                    
                    // Grid of months (3 per row)
                    for (row in 0..3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            for (col in 0..2) {
                                val monthIndex = row * 3 + col
                                if (monthIndex < months.size) {
                                    val (index, monthName) = months[monthIndex]
                                    
                                    val cal = Calendar.getInstance()
                                    cal.time = selectedDate
                                    val isSelected = cal.get(Calendar.MONTH) == index
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 4.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                            .clickable {
                                                val newCal = Calendar.getInstance()
                                                newCal.time = selectedDate
                                                newCal.set(Calendar.MONTH, index)
                                                onDateSelected(newCal.time)
                                                isExpanded = false
                                            }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = monthName,
                                            color = if (isSelected) Color.White 
                                                    else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
} 