package com.example.rng_sipp.Shop.Cart

import java.text.NumberFormat
import java.util.Locale

data class CartItem(
    val id: String,
    val name: String,
    val variant: String,
    val price: Long,
    val imageRes: Int,
    var quantity: Int = 1
)

object CartManager {
    private val items = mutableListOf<CartItem>()

    fun addItem(item: CartItem) {
        val existing = items.find { it.name == item.name && it.variant == item.variant }
        if (existing != null) {
            existing.quantity += item.quantity
        } else {
            items.add(item)
        }
    }

    fun getItems(): MutableList<CartItem> = items

    fun updateQuantity(index: Int, delta: Int) {
        if (index in items.indices) {
            val newQty = items[index].quantity + delta
            if (newQty > 0) {
                items[index].quantity = newQty
            }
        }
    }

    fun removeItem(index: Int) {
        if (index in items.indices) {
            items.removeAt(index)
        }
    }

    fun clearCart() {
        items.clear()
    }

    fun getTotalQuantity(): Int = items.sumOf { it.quantity }

    fun getSubtotal(): Long = items.sumOf { it.price * it.quantity }

    fun formatRupiah(amount: Long): String {
        val formatter = NumberFormat.getInstance(Locale("id", "ID"))
        return "Rp ${formatter.format(amount)}"
    }

    fun parsePrice(priceText: String): Long {
        val clean = priceText.replace("[^0-9]".toRegex(), "")
        return clean.toLongOrNull() ?: 2450000L
    }
}