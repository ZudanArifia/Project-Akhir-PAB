package com.example.manajemenuang.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.manajemenuang.ui.theme.getGradientStartColor
import com.example.manajemenuang.ui.theme.getGradientEndColor

// Sebuah composable yang menyediakan latar belakang gradien untuk layar berdasarkan tema yang dipilih
// Menggunakan remember untuk menghindari pembuatan brush berulang
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    startColor: Color = getGradientStartColor(),
    endColor: Color = getGradientEndColor(),
    content: @Composable BoxScope.() -> Unit
) {
    // Menggunakan remember untuk menghindari pembuatan brush baru pada setiap rekomposisi
    val gradientBrush = remember(startColor, endColor) {
        Brush.verticalGradient(
            colors = listOf(startColor, endColor)
        )
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush),
        content = content
    )
} 