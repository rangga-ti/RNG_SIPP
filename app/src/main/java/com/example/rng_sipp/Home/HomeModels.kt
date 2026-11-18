package com.example.rng_sipp.Home

data class QuickMenuItem(
    val iconRes: Int,
    val title: String
)

data class Product(
    val name: String,
    val category: String,
    val price: String,
    val rating: String,
    val soldCount: String,
    val badge: String,
    val imageRes: Int,
    val isPromo: Boolean = false,
    val isPopular: Boolean = false
)