package com.example.manajemenuang.utils

import java.text.NumberFormat
import java.util.Locale

// Fungsi utilitas untuk memformat angka sebagai mata uang Rupiah
fun formatAsRupiah(amount: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return numberFormat.format(amount)
} 