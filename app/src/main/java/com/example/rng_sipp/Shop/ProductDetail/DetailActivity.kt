package com.example.rng_sipp.Shop.ProductDetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.R
import com.example.rng_sipp.Shop.Payment.PaymentBottomSheetDialog
import com.example.rng_sipp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

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

        // Inisialisasi Image Slider produk
        setupProductImageSlider()

        // Listener tombol back header
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "$productName ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        }

        // Listener tombol beli -> Tampilkan Bottom Sheet
        binding.btnOrderNow.setOnClickListener {
            showPaymentBottomSheet(productName, productPrice)
        }
    }

    // Fungsi khusus setup ViewPager2 slider gambar
    private fun setupProductImageSlider() {
        val imageList = listOf(
            R.drawable.sofasatu,
            R.drawable.sofadua,
            R.drawable.sofatiga
        )

        val sliderAdapter = ImageSliderAdapter(imageList)
        binding.vpProductImage.adapter = sliderAdapter

        // Hubungkan ViewPager2 ke TabLayout untuk indikator titik
        TabLayoutMediator(binding.tlImageIndicator, binding.vpProductImage) { _, _ -> }.attach()
    }

    // Fungsi menampilkan BottomSheet Metode Pembayaran
    private fun showPaymentBottomSheet(name: String, price: String) {
        val paymentDialog = PaymentBottomSheetDialog { selectedPayment ->
            showOrderConfirmationDialog(name, price, selectedPayment)
        }
        paymentDialog.show(supportFragmentManager, "PaymentBottomSheet")
    }

    // Fungsi dialog konfirmasi pesanan
    private fun showOrderConfirmationDialog(name: String, price: String, paymentMethod: String) {
        val orderMessage = """
            Detail Pesanan:
            
            • Produk: $name
            • Total: $price
            • Pembayaran: $paymentMethod
            
            Lanjutkan transaksi?
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pemesanan")
            .setMessage(orderMessage)
            .setPositiveButton("Ya, Bayar") { dialog, _ ->
                Toast.makeText(this, "Pesanan berhasil dibuat via $paymentMethod!", Toast.LENGTH_LONG).show()
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