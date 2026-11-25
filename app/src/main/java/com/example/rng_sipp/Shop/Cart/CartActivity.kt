package com.example.rng_sipp.Shop.Cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rng_sipp.Shop.Payment.PaymentActivity
import com.example.rng_sipp.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackCart.setOnClickListener { finish() }

        val adapter = CartAdapter(CartManager.getItems()) {
            updateSummary()
        }
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = adapter

        updateSummary()

        binding.btnProceedCheckout.setOnClickListener {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java).apply {
                    putExtra("DIRECT_ORDER", false)
                }
                startActivity(intent)
            }
        }
    }

    private fun updateSummary() {
        val totalQty = CartManager.getTotalQuantity()
        val subtotal = CartManager.getSubtotal()
        val formatted = CartManager.formatRupiah(subtotal)

        binding.tvCartCountHeader.text = "$totalQty barang di keranjang"
        binding.tvSubtotalLabel.text = "Subtotal ($totalQty barang)"
        binding.tvCartSubtotal.text = formatted
        binding.tvCartTotal.text = formatted
    }
}