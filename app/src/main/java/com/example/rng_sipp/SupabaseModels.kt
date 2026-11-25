package com.example.rng_sipp

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Long? = null,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val phone: String,
    val password: String
)

data class ProductDto(
    val id: Long? = null,
    val name: String,
    val category: String,
    val price: Long,
    val stock: Int,
    val material: String
)

data class OrderDto(
    val id: Long? = null,
    @SerializedName("order_code") val orderCode: String,
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("product_name") val productName: String,
    val quantity: Int,
    @SerializedName("total_price") val totalPrice: Long,
    @SerializedName("payment_method") val paymentMethod: String,
    val status: String
)