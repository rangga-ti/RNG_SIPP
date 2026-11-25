package com.example.rng_sipp.Shop.Payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rng_sipp.BaseActivity
import com.example.rng_sipp.OrderDto
import com.example.rng_sipp.R
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.Shop.OrderStatus.OrderManager
import com.example.rng_sipp.Shop.OrderStatus.OrderStatusActivity
import com.example.rng_sipp.SupabaseClient
import com.example.rng_sipp.databinding.ActivityPaymentBinding
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var totalAmount = 0L
    private var isDirectOrder = false
    private var voucherApplied = false

    private var summaryName = "Sofa Ruang Tamu Skandinavia"
    private var summaryQty = 1
    private var summaryImage = R.drawable.sofasatu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackPayment.setOnClickListener { finish() }

        isDirectOrder = intent.getBooleanExtra("DIRECT_ORDER", false)

        if (isDirectOrder) {
            summaryName = intent.getStringExtra("ITEM_NAME") ?: "Sofa Minimalis"
            val variant = intent.getStringExtra("ITEM_VARIANT") ?: "Emerald • 3 Seater"
            summaryImage = intent.getIntExtra("ITEM_IMAGE", R.drawable.sofasatu)
            summaryQty = 1
            totalAmount = intent.getLongExtra("ITEM_PRICE", 3000000L)

            binding.ivPayProduct.setImageResource(summaryImage)
            binding.tvPayProductName.text = summaryName
            binding.tvPayProductQty.text = "1 item • $variant"
        } else {
            val items = CartManager.getItems()
            val first = items.firstOrNull()
            totalAmount = CartManager.getSubtotal()
            summaryQty = CartManager.getTotalQuantity()

            if (first != null) {
                summaryImage = first.imageRes
                summaryName = if (items.size > 1) {
                    "${first.name} (+${items.size - 1} produk lainnya)"
                } else {
                    first.name
                }
                binding.ivPayProduct.setImageResource(summaryImage)
                binding.tvPayProductName.text = summaryName
                binding.tvPayProductQty.text = "$summaryQty item dari Keranjang"
            }
        }

        updatePriceDisplay()

        binding.btnApplyVoucher.setOnClickListener {
            val code = binding.etVoucherCode.text.toString().trim()
            if (code.isNotEmpty() && !voucherApplied && totalAmount > 100000L) {
                totalAmount -= 100000L
                voucherApplied = true
                updatePriceDisplay()
                Toast.makeText(this, "Voucher berhasil! Potongan Rp 100.000", Toast.LENGTH_SHORT).show()
            } else if (voucherApplied) {
                Toast.makeText(this, "Voucher sudah digunakan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Masukkan kode voucher terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPayNow.setOnClickListener {
            val method = when (binding.rgPaymentMethod.checkedRadioButtonId) {
                R.id.rbEwallet -> "E-Wallet"
                R.id.rbCreditCard -> "Kartu Kredit"
                else -> "Transfer Bank (BCA)"
            }

            // Ambil nama user dari SharedPreferences (hasil register)
            val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            val customerName = sharedPref.getString("reg_nama", "Pelanggan SIPP")
                ?.ifBlank { "Pelanggan SIPP" } ?: "Pelanggan SIPP"

            val orderCode = "INV/2026/SIPP-${(1000..9999).random()}"

            // Simpan ke lokal OrderManager
            OrderManager.addOrder(
                productName = summaryName,
                itemCount = summaryQty,
                totalAmount = totalAmount,
                paymentMethod = method,
                imageRes = summaryImage
            )

            // Kirim (CREATE) ke tabel 'orders' di Supabase
            binding.btnPayNow.isEnabled = false
            binding.btnPayNow.text = "MEMPROSES PEMBAYARAN..."

            lifecycleScope.launch {
                try {
                    val newOrder = OrderDto(
                        orderCode = orderCode,
                        customerName = customerName,
                        productName = summaryName,
                        quantity = summaryQty,
                        totalPrice = totalAmount,
                        paymentMethod = method,
                        status = "Sedang Dikemas"
                    )
                    val response = SupabaseClient.api.createOrder(newOrder)
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@PaymentActivity,
                            "Info Supabase: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@PaymentActivity,
                        "Gagal sinkron cloud: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    if (!isDirectOrder) {
                        CartManager.clearCart()
                    }
                    binding.btnPayNow.isEnabled = true
                    updatePriceDisplay()
                    showSuccessDialog(method)
                }
            }
        }
    }

    private fun showSuccessDialog(method: String) {
        AlertDialog.Builder(this)
            .setTitle("Pembayaran Berhasil!")
            .setMessage(
                "Pesanan Anda sebesar ${CartManager.formatRupiah(totalAmount)} melalui $method telah dikonfirmasi dan tersimpan di Supabase."
            )
            .setCancelable(false)
            .setPositiveButton("Lihat Status Pesanan") { _, _ ->
                startActivity(Intent(this, OrderStatusActivity::class.java))
                finish()
            }
            .setNegativeButton("Ke Beranda") { _, _ ->
                val intent = Intent(this, BaseActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun updatePriceDisplay() {
        val formatted = CartManager.formatRupiah(totalAmount)
        binding.tvPayProductTotal.text = formatted
        binding.btnPayNow.text = "BAYAR SEKARANG - $formatted"
    }
}