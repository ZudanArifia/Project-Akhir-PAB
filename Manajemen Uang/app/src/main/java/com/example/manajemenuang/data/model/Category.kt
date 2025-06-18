package com.example.manajemenuang.data.model


// Class untuk opsi kategori di transaksi
data class Category(
    val name: String,
    val icon: Int? = null
)


// Predefined categories
object CategoryList {
    val expenseCategories = listOf(
        "Makanan",
        "Listrik",
        "Belanja",
        "Jajan",
        "Bensin",
        "Pulsa",
        "Transportasi",
        "Makan dan Minum",
        "Belanja Bulanan"
    )

    val incomeCategories = listOf(
        "Gaji",
        "Bonus",
        "Hadiah",
        "Penjualan",
        "Lainnya"
    )
} 