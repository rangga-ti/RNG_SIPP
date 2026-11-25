package com.example.rng_sipp.Shop.OrderStatus

import com.example.rng_sipp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OrderItem(
    val orderId: String,
    val productName: String,
    val itemCount: Int,
    val totalAmount: Long,
    val paymentMethod: String,
    val date: String,
    val imageRes: Int,
    var status: String // "Sedang Dikemas", "Dalam Pengiriman", "Selesai"
)

object OrderManager {
    private val orders = mutableListOf(
        OrderItem(
            orderId = "INV/2026/SIPP-1042",
            productName = "Kursi Lounge Nordik Kayu Jati",
            itemCount = 1,
            totalAmount = 1275000L,
            paymentMethod = "Transfer Bank (BCA)",
            date = "22 Nov 2026",
            imageRes = R.drawable.sofadua,
            status = "Selesai"
        )
    )

    fun addOrder(
        productName: String,
        itemCount: Int,
        totalAmount: Long,
        paymentMethod: String,
        imageRes: Int
    ) {
        val randomNum = (2000..9999).random()
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
        orders.add(
            0,
            OrderItem(
                orderId = "INV/2026/SIPP-$randomNum",
                productName = productName,
                itemCount = itemCount,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                date = currentDate,
                imageRes = imageRes,
                status = "Sedang Dikemas"
            )
        )
    }

    fun getOrders(): MutableList<OrderItem> = orders

    fun getActiveOrderCount(): Int = orders.count { it.status != "Selesai" }
}