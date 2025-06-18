package com.example.manajemenuang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.manajemenuang.navigation.AppNavigation
import com.example.manajemenuang.ui.theme.ManajemenUangTheme
import com.example.manajemenuang.ui.theme.ThemeColors
import com.example.manajemenuang.ui.theme.ThemeManager

// Main activity yang memulai aplikasi dan menyiapkan navigasi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inisialisasi tema default jika belum diatur
        if (savedInstanceState == null) {
            ThemeManager.currentThemeColors = ThemeColors.BLUE_TEAL
        }
        
        setContent {
            // Menggunakan tema aplikasi yang akan otomatis mendeteksi perubahan tema
            ManajemenUangTheme {
                // Menggunakan NavController tunggal
                val navController = rememberNavController()
                
                // Konten utama aplikasi
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    // Menyiapkan navigasi menggunakan komponen AppNavigation
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}