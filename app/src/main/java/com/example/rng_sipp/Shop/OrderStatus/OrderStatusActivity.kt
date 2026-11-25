package com.example.rng_sipp.Shop.OrderStatus

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rng_sipp.OrderDto
import com.example.rng_sipp.R
import com.example.rng_sipp.SupabaseClient
import com.example.rng_sipp.databinding.ActivityOrderStatusBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderStatusBinding
    private lateinit var adapter: OrderAdapter
    private val cloudOrders = mutableListOf<OrderDto>()
    private val mappedOrders = mutableListOf<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackOrder.setOnClickListener { finish() }

        adapter = OrderAdapter(emptyList()) { clickedItem ->
            val targetDto = cloudOrders.find { it.orderCode == clickedItem.orderId }
            if (targetDto != null) {
                showManageOrderDialog(targetDto)
            }
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter

        binding.tabOrderFilter.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterOrders(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Ambil data dari Supabase saat halaman dibuka
        loadOrdersFromSupabase()
    }

    // READ: Ambil seluruh pesanan dari tabel 'orders' Supabase
    private fun loadOrdersFromSupabase() {
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.getAllOrders()
                if (response.isSuccessful) {
                    cloudOrders.clear()
                    cloudOrders.addAll(response.body().orEmpty())

                    mappedOrders.clear()
                    mappedOrders.addAll(
                        cloudOrders.map { dto ->
                            OrderItem(
                                orderId = dto.orderCode,
                                productName = dto.productName,
                                itemCount = dto.quantity,
                                totalAmount = dto.totalPrice,
                                paymentMethod = dto.paymentMethod,
                                date = "Pemesan: ${dto.customerName}",
                                imageRes = R.drawable.sofasatu,
                                status = dto.status
                            )
                        }
                    )

                    filterOrders(binding.tabOrderFilter.selectedTabPosition)
                } else {
                    Toast.makeText(
                        this@OrderStatusActivity,
                        "Gagal memuat pesanan (${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@OrderStatusActivity,
                    "Koneksi Supabase gagal: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // UPDATE & DELETE: Kelola status atau hapus pesanan di Supabase
    private fun showManageOrderDialog(order: OrderDto) {
        val options = arrayOf(
            "Update Status: Dalam Pengiriman",
            "Update Status: Selesai (Pesanan Diterima)",
            "Hapus / Batalkan Pesanan"
        )

        AlertDialog.Builder(this)
            .setTitle("Detail Pelacakan (${order.orderCode})")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> updateOrderStatusInSupabase(order, "Dalam Pengiriman")
                    1 -> updateOrderStatusInSupabase(order, "Selesai")
                    2 -> deleteOrderInSupabase(order)
                }
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun updateOrderStatusInSupabase(order: OrderDto, newStatus: String) {
        val orderId = order.id ?: return
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.updateOrderStatus(
                    idFilter = "eq.$orderId",
                    body = mapOf("status" to newStatus)
                )
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@OrderStatusActivity,
                        "Status diperbarui menjadi: $newStatus",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadOrdersFromSupabase()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrderStatusActivity, "Gagal update: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteOrderInSupabase(order: OrderDto) {
        val orderId = order.id ?: return
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.deleteOrder(idFilter = "eq.$orderId")
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@OrderStatusActivity,
                        "Pesanan ${order.orderCode} dihapus dari Supabase!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadOrdersFromSupabase()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrderStatusActivity, "Gagal hapus: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterOrders(tabPosition: Int) {
        val filtered = when (tabPosition) {
            1 -> mappedOrders.filter { it.status != "Selesai" }
            2 -> mappedOrders.filter { it.status == "Selesai" }
            else -> mappedOrders
        }
        adapter.updateData(filtered)
    }
}