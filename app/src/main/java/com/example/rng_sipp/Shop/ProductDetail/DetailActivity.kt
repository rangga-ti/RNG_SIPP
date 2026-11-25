package com.example.rng_sipp.Shop.ProductDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.Shop.Cart.CartActivity
import com.example.rng_sipp.Shop.Cart.CartItem
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.R
import com.example.rng_sipp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.rng_sipp.Shop.Payment.PaymentActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val TAG = "DetailActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate dipanggil")

        // Tangkap data intent (mendukung key lama maupun baru)
        val productName = intent.getStringExtra("EXTRA_PRODUCT_NAME")
            ?: intent.getStringExtra("PRODUCT_NAME")
            ?: "Sofa Ruang Tamu Skandinavia"

        val priceText = intent.getStringExtra("EXTRA_PRODUCT_PRICE")
            ?: intent.getStringExtra("PRODUCT_PRICE")
            ?: "Rp 3.000.000"

        val productPrice = CartManager.parsePrice(priceText)
        val productImage = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.sofasatu)

        binding.tvDetailTitle.text = productName
        binding.tvDetailPrice.text = CartManager.formatRupiah(productPrice)

        // Inisialisasi Image Slider produk (tetap menggunakan slider aslimu)
        setupProductImageSlider(productImage)

        // Tombol Back di header
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Ikon Keranjang di pojok kanan atas header
        binding.btnCartShortcut.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Tombol 1: Tambah ke Keranjang -> simpan item & buka halaman daftar keranjang
        binding.btnAddToCart.setOnClickListener {
            val variant = getSelectedVariant()
            CartManager.addItem(
                CartItem(
                    id = System.currentTimeMillis().toString(),
                    name = productName,
                    variant = variant,
                    price = productPrice,
                    imageRes = productImage,
                    quantity = 1
                )
            )
            Toast.makeText(this, "$productName ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Tombol 2: Pesan Sekarang -> langsung masuk ke halaman Metode Pembayaran
        binding.btnOrderNow.setOnClickListener {
            val variant = getSelectedVariant()
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("DIRECT_ORDER", true)
                putExtra("ITEM_NAME", productName)
                putExtra("ITEM_VARIANT", variant)
                putExtra("ITEM_PRICE", productPrice)
                putExtra("ITEM_IMAGE", productImage)
            }
            startActivity(intent)
        }
    }

    // Ambil pilihan Warna & Ukuran dari RadioButton
    private fun getSelectedVariant(): String {
        val color = if (binding.rbEmerald.isChecked) "Emerald" else "Wood"
        val size = if (binding.rb3Seater.isChecked) "3 Seater" else "2 Seater"
        return "$color • $size"
    }

    // Setup ViewPager2 slider gambar + foto produk yang diklik di posisi pertama
    private fun setupProductImageSlider(mainImage: Int) {
        val imageList = listOf(
            mainImage,
            R.drawable.sofadua,
            R.drawable.sofatiga
        ).distinct()

        val sliderAdapter = ImageSliderAdapter(imageList)
        binding.vpProductImage.adapter = sliderAdapter

        TabLayoutMediator(binding.tlImageIndicator, binding.vpProductImage) { _, _ -> }.attach()
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