package com.example.manajemenuang.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.manajemenuang.navigation.NavDestinations
import com.example.manajemenuang.ui.theme.SurfaceLight
import com.example.manajemenuang.ui.theme.TextSecondary
import com.example.manajemenuang.ui.theme.getNavigationColor

// Bottom navigation bar dengan item navigasi utama
// Menggunakan ikon yang tersedia di Icons.Filled dan mendeteksi halaman aktif
// Bottom navigation bar dengan item navigasi utama
@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Mendefinisikan item navigasi dengan ikon yang tersedia
    val items = remember {
        listOf(
            NavigationItem("Transaksi", Icons.Filled.List, NavDestinations.TRANSACTIONS_SCREEN),
            NavigationItem("Dompet", Icons.Filled.AccountBalance, NavDestinations.HOME_SCREEN),
            NavigationItem("Laporan", Icons.Filled.BarChart, NavDestinations.REPORT_SCREEN),
            NavigationItem("Pengaturan", Icons.Filled.Settings, NavDestinations.SETTINGS_SCREEN)
        )
    }
    
    // Mendapatkan destinasi saat ini dari NavController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Mendapatkan warna navigasi berdasarkan tema
    val navigationColor = getNavigationColor()
    
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = SurfaceLight,
        tonalElevation = 0.dp // Menghilangkan bayangan untuk mengurangi beban render
    ) {
        items.forEach { item ->
            // Cek apakah rute saat ini cocok dengan item navigasi
            val isSelected = currentDestination?.hierarchy?.any { 
                when (it.route) {
                    item.route -> true
                    NavDestinations.DAILY_REPORT_SCREEN, 
                    NavDestinations.MONTHLY_REPORT_SCREEN -> item.route == NavDestinations.REPORT_SCREEN // Laporan tab aktif untuk semua screen laporan
                    else -> false
                }
            } ?: false
            
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    ) 
                },
                label = { 
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodySmall
                    ) 
                },
                selected = isSelected,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            // Navigasi yang lebih efisien untuk mengurangi flickering
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = navigationColor,
                    selectedTextColor = navigationColor,
                    indicatorColor = SurfaceLight,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

// Data class untuk item navigasi
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) 