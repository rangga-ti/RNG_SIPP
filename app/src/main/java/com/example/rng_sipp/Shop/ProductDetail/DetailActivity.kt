package com.example.rng_sipp.Shop.ProductDetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val TAG = "DetailActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate dipanggil")

        // Tangkap data intent
        val productName = intent.getStringExtra("EXTRA_PRODUCT_NAME") ?: "Sofa Ruang Tamu Skandinavia"
        val productPrice = intent.getStringExtra("EXTRA_PRODUCT_PRICE") ?: "Rp3.000.000"

        binding.tvDetailTitle.text = productName
        binding.tvDetailPrice.text = productPrice

        // Listener tombol back custom header
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "$productName ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        }

        binding.btnOrderNow.setOnClickListener {
            showOrderConfirmationDialog(productName, productPrice)
        }
    }

    private fun showOrderConfirmationDialog(name: String, price: String) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pemesanan")
            .setMessage("Apakah Anda ingin langsung memesan $name seharga $price?")
            .setPositiveButton("Ya, Pesan") { dialog, _ ->
                Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart dipanggil")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume dipanggil")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause dipanggil")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop dipanggil")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy dipanggil")
    }
}