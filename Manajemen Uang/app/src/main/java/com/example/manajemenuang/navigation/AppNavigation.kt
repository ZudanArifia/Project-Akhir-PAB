package com.example.manajemenuang.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manajemenuang.data.model.TransactionType
import com.example.manajemenuang.ui.screens.AddTransactionScreen
import com.example.manajemenuang.ui.screens.DailyReportScreen
import com.example.manajemenuang.ui.screens.EditTransactionScreen
import com.example.manajemenuang.ui.screens.HomeScreen
import com.example.manajemenuang.ui.screens.MonthlyReportScreen
import com.example.manajemenuang.ui.screens.ReportScreen
import com.example.manajemenuang.ui.screens.SettingsScreen
import com.example.manajemenuang.ui.screens.TransactionsScreen

// Tujuan navigasi yang digunakan dalam aplikasi
object NavDestinations {
    const val HOME_SCREEN = "home"
    const val TRANSACTIONS_SCREEN = "transactions"
    const val ADD_TRANSACTION_SCREEN = "add_transaction"
    const val ADD_INCOME_SCREEN = "add_income"
    const val ADD_EXPENSE_SCREEN = "add_expense"
    const val EDIT_TRANSACTION_SCREEN = "edit_transaction"
    const val DAILY_REPORT_SCREEN = "daily_report"
    const val MONTHLY_REPORT_SCREEN = "monthly_report"
    const val REPORT_SCREEN = "report"
    const val SETTINGS_SCREEN = "settings"
}

// Komponen navigasi utama untuk aplikasi
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.HOME_SCREEN
) {
    val actions = remember(navController) {
        NavActions(navController)
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Definisi rute dengan lazy loading untuk screen
        composable(NavDestinations.HOME_SCREEN) {
            HomeScreen(navController)
        }
        
        composable(NavDestinations.TRANSACTIONS_SCREEN) {
            TransactionsScreen(navController)
        }
        
        composable(NavDestinations.ADD_TRANSACTION_SCREEN) {
            AddTransactionScreen(
                navController = navController,
                initialTransactionType = TransactionType.EXPENSE
            )
        }
        
        composable(NavDestinations.ADD_INCOME_SCREEN) {
            AddTransactionScreen(
                navController = navController,
                initialTransactionType = TransactionType.INCOME
            )
        }
        
        composable(NavDestinations.ADD_EXPENSE_SCREEN) {
            AddTransactionScreen(
                navController = navController,
                initialTransactionType = TransactionType.EXPENSE
            )
        }
        
        composable(
            route = NavDestinations.EDIT_TRANSACTION_SCREEN + "/{transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId") ?: ""
            EditTransactionScreen(
                navController = navController,
                transactionId = transactionId
            )
        }
        
        composable(NavDestinations.DAILY_REPORT_SCREEN) {
            DailyReportScreen(navController)
        }
        
        composable(NavDestinations.MONTHLY_REPORT_SCREEN) {
            MonthlyReportScreen(navController)
        }
        
        composable(NavDestinations.REPORT_SCREEN) {
            ReportScreen(navController)
        }
        
        composable(NavDestinations.SETTINGS_SCREEN) {
            SettingsScreen(navController)
        }
    }
}

// Helper class untuk navigasi yang lebih efisien
class NavActions(private val navController: NavHostController) {
    
    val navigateToHome: () -> Unit = {
        navController.navigateToTopLevel(NavDestinations.HOME_SCREEN)
    }
    
    val navigateToTransactions: () -> Unit = {
        navController.navigateToTopLevel(NavDestinations.TRANSACTIONS_SCREEN)
    }
    
    val navigateToAddTransaction: () -> Unit = {
        navController.navigate(NavDestinations.ADD_TRANSACTION_SCREEN)
    }
    
    val navigateToAddIncome: () -> Unit = {
        navController.navigate(NavDestinations.ADD_INCOME_SCREEN)
    }
    
    val navigateToAddExpense: () -> Unit = {
        navController.navigate(NavDestinations.ADD_EXPENSE_SCREEN)
    }
    
    val navigateToReport: () -> Unit = {
        navController.navigateToTopLevel(NavDestinations.REPORT_SCREEN)
    }
    
    val navigateToMonthlyReport: () -> Unit = {
        navController.navigate(NavDestinations.MONTHLY_REPORT_SCREEN)
    }
    
    val navigateToDailyReport: () -> Unit = {
        navController.navigate(NavDestinations.DAILY_REPORT_SCREEN)
    }
    
    val navigateToSettings: () -> Unit = {
        navController.navigateToTopLevel(NavDestinations.SETTINGS_SCREEN)
    }
}


// Extension function untuk navigasi ke level teratas
// dengan pengaturan yang optimal untuk menghindari flickering

private fun NavHostController.navigateToTopLevel(route: String) {
    this.navigate(route) {
        // Hindari tumpukan navigasi yang berlebihan
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        // Hindari duplikasi tujuan yang sama
        launchSingleTop = true
        // Pulihkan state saat kembali ke tujuan yang sudah pernah dikunjungi
        restoreState = true
    }
} 