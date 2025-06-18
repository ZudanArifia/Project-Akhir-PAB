package com.example.manajemenuang.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Menyimpan preferensi tema yang dipilih dengan mutableStateOf
// untuk memastikan rekomposisi saat tema berubah
object ThemeManager {
    var currentThemeColors by mutableStateOf(ThemeColors.BLUE_TEAL)
}

// Enum pilihan tema warna
enum class ThemeColors {
    BLUE_TEAL,     // Tema biru-teal original
    BLACK_GRAY     // Tema hitam ke abu-abu
}

// Color schemes untuk tema Black Gray
private val BlackGrayLightColorScheme = lightColorScheme(
    primary = Color(0xFF121212),         // Hitam
    secondary = Color(0xFF424242),       // Abu-abu gelap
    tertiary = Color(0xFF757575),        // Abu-abu sedang
    background = Color(0xFFFFFFFF),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF121212),    // Hitam
    onSurface = Color(0xFF121212)        // Hitam
)

// Background gradients untuk tema Black Gray
val BlackGrayStart = Color(0xFF121212)   // Hitam
val BlackGrayEnd = Color(0xFF757575)     // Abu-abu

// Color schemes untuk light and dark themes
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryTeal,
    tertiary = PrimaryLightTeal,
    background = SurfaceLight,
    surface = SurfaceLight,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryTeal,
    tertiary = PrimaryLightTeal
    // We'll focus on light theme for now
)

// Fungsi helper untuk memilih scheme warna berdasarkan tema yang dipilih
@Composable
private fun getColorScheme(themeColors: ThemeColors, darkTheme: Boolean) = when (themeColors) {
    ThemeColors.BLUE_TEAL -> if (darkTheme) DarkColorScheme else LightColorScheme
    ThemeColors.BLACK_GRAY -> BlackGrayLightColorScheme
}

// Fungsi untuk mendapatkan warna gradient start berdasarkan tema yang dipilih
@Composable
fun getGradientStartColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> BackgroundStart
        ThemeColors.BLACK_GRAY -> BlackGrayStart
    }
}


// Fungsi untuk mendapatkan warna gradient end berdasarkan tema yang dipilih
@Composable
fun getGradientEndColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> BackgroundEnd
        ThemeColors.BLACK_GRAY -> BlackGrayEnd
    }
}

// Fungsi untuk mendapatkan warna aksen berdasarkan tema yang dipilih
// Digunakan untuk tombol floating action, tabs, dll
@Composable
fun getAccentColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> PrimaryTeal
        ThemeColors.BLACK_GRAY -> Color(0xFF424242) // Abu-abu gelap
    }
}


// Fungsi untuk mendapatkan warna navigasi berdasarkan tema yang dipilih
// Digunakan untuk bottom navigation, tabs, dll
@Composable
fun getNavigationColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> PrimaryBlue
        ThemeColors.BLACK_GRAY -> Color(0xFF121212) // Hitam
    }
}


// Fungsi untuk mendapatkan warna status bar berdasarkan tema yang dipilih

@Composable
fun getStatusBarColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> PrimaryBlue
        ThemeColors.BLACK_GRAY -> Color(0xFF121212) // Hitam
    }
}


// Fungsi untuk mendapatkan warna kategori berdasarkan tema yang dipilih
@Composable
fun getCategoryButtonColor(): Color {
    return when (ThemeManager.currentThemeColors) {
        ThemeColors.BLUE_TEAL -> PrimaryTeal
        ThemeColors.BLACK_GRAY -> Color(0xFF424242) // Abu-abu gelap
    }
}

// Theme/Tema utama untuk aplikasi
@Composable
fun ManajemenUangTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Mendapatkan tema yang dipilih
    val currentThemeColors = remember { ThemeManager.currentThemeColors }
    
    // Mendapatkan color scheme berdasarkan tema yang dipilih
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getColorScheme(currentThemeColors, darkTheme)
    }
    
    // Mengatur warna status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Use colorScheme.primary directly for status bar to avoid composable call outside of composable context
            val statusBarColor = when (ThemeManager.currentThemeColors) {
                ThemeColors.BLUE_TEAL -> PrimaryBlue
                ThemeColors.BLACK_GRAY -> Color(0xFF121212) // Hitam
            }
            window.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}