package com.example.manajemenuang.data.model

import java.util.Date
import java.util.UUID


// class untuk representasi transaksi

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val date: Date = Date(),
    val amount: Double = 0.0,
    val title: String = "",
    val category: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val paymentMethod: String = "",
    val description: String = ""
)


// Enum class untuk membedakan antara pemasukan dan pengeluaran

enum class TransactionType {
    INCOME,
    EXPENSE
} 