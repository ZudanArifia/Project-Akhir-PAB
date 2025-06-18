package com.example.manajemenuang.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Bentuk yang digunakan untuk komponen UI dalam aplikasi
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp)
)

// Bentuk kustom tambahan
val CardShape = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val InputFieldShape = RoundedCornerShape(12.dp)
val BottomBarShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
val FloatingActionButtonShape = RoundedCornerShape(percent = 50) 