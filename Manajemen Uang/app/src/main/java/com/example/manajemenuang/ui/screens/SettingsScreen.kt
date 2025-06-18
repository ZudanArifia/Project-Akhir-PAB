package com.example.manajemenuang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.manajemenuang.ui.components.BottomNavigationBar
import com.example.manajemenuang.ui.components.GradientBackground
import com.example.manajemenuang.ui.theme.BackgroundEnd
import com.example.manajemenuang.ui.theme.BackgroundStart
import com.example.manajemenuang.ui.theme.BlackGrayEnd
import com.example.manajemenuang.ui.theme.BlackGrayStart
import com.example.manajemenuang.ui.theme.ThemeColors
import com.example.manajemenuang.ui.theme.ThemeManager
import com.example.manajemenuang.ui.theme.TextWhite

// Layar untuk pengaturan aplikasi
@Composable
fun SettingsScreen(navController: NavController) {
    // Menggunakan state lokal untuk menghindari rekomposisi berlebihan
    var currentTheme by remember { mutableStateOf(ThemeManager.currentThemeColors) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        GradientBackground(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    
                    Text(
                        text = "Pengaturan",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Settings Content
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tema Aplikasi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Theme Color Options
                        Text(
                            text = "Pilih Warna Tema",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Theme color selectors - menggunakan remember untuk brush
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Blue-Teal Theme (Default)
                            ThemeColorOption(
                                startColor = BackgroundStart,
                                endColor = BackgroundEnd,
                                isSelected = currentTheme == ThemeColors.BLUE_TEAL,
                                onClick = { 
                                    currentTheme = ThemeColors.BLUE_TEAL
                                    ThemeManager.currentThemeColors = ThemeColors.BLUE_TEAL
                                }
                            )
                            
                            // Black Gray Theme
                            ThemeColorOption(
                                startColor = BlackGrayStart,
                                endColor = BlackGrayEnd,
                                isSelected = currentTheme == ThemeColors.BLACK_GRAY,
                                onClick = { 
                                    currentTheme = ThemeColors.BLACK_GRAY
                                    ThemeManager.currentThemeColors = ThemeColors.BLACK_GRAY
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Nama Tema/Theme
                        Text(
                            text = when (currentTheme) {
                                ThemeColors.BLUE_TEAL -> "Tema: Biru-Teal (Default)"
                                ThemeColors.BLACK_GRAY -> "Tema: Hitam Abu-abu"
                                else -> "Tema: Biru-Teal (Default)"
                            },
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

// Komponen untuk memilih warna tema
@Composable
fun ThemeColorOption(
    startColor: Color,
    endColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Menggunakan remember untuk brush agar tidak dibuat ulang pada setiap rekomposisi
    val gradientBrush = remember(startColor, endColor) {
        Brush.verticalGradient(colors = listOf(startColor, endColor))
    }
    
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(brush = gradientBrush)
            .clickable(onClick = onClick)
            .padding(if (isSelected) 3.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0).copy(alpha = 0.3f))  // Light gray on selection
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(brush = gradientBrush)
                )
            }
        }
    }
} 