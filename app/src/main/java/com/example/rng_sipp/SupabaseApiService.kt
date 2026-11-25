package com.example.rng_sipp

import retrofit2.Response
import retrofit2.http.*

interface SupabaseApiService {

    // ==================== 1. TABEL USERS (REGISTER) ====================
    @Headers("Prefer: return=representation")
    @POST("rest/v1/users")
    suspend fun registerUser(@Body user: UserDto): Response<List<UserDto>>

    @GET("rest/v1/users")
    suspend fun getUsers(@Query("select") select: String = "*"): Response<List<UserDto>>

    // ==================== 2. CRUD TABEL PRODUCTS ====================
    @GET("rest/v1/products")
    suspend fun getAllProducts(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.desc"
    ): Response<List<ProductDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/products")
    suspend fun createProduct(@Body product: ProductDto): Response<List<ProductDto>>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/products")
    suspend fun updateProduct(
        @Query("id") idFilter: String,
        @Body product: ProductDto
    ): Response<List<ProductDto>>

    @DELETE("rest/v1/products")
    suspend fun deleteProduct(
        @Query("id") idFilter: String
    ): Response<Unit>

    // ==================== 3. CRUD TABEL ORDERS ====================
    @GET("rest/v1/orders")
    suspend fun getAllOrders(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.desc"
    ): Response<List<OrderDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/orders")
    suspend fun createOrder(@Body order: OrderDto): Response<List<OrderDto>>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/orders")
    suspend fun updateOrderStatus(
        @Query("id") idFilter: String,
        @Body body: Map<String, String>
    ): Response<List<OrderDto>>

    @DELETE("rest/v1/orders")
    suspend fun deleteOrder(
        @Query("id") idFilter: String
    ): Response<Unit>
}