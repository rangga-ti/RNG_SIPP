package com.example.rng_sipp.Shop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rng_sipp.R
import com.example.rng_sipp.Shop.Cart.CartActivity
import com.example.rng_sipp.Shop.Cart.CartItem
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.Shop.OrderStatus.OrderManager
import com.example.rng_sipp.Shop.OrderStatus.OrderStatusActivity
import com.example.rng_sipp.Shop.Product.ManageProductActivity
import com.example.rng_sipp.Shop.ProductDetail.DetailActivity
import com.example.rng_sipp.SupabaseClient
import com.example.rng_sipp.databinding.FragmentShopBinding
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShopProductAdapter
    private var selectedCategory = "Semua"
    private var searchQuery = ""

    private val displayedProducts = mutableListOf(
        ShopProduct("Sofa Ruang Tamu Skandinavia", "Sofa", 3000000L, "4.9", "142", R.drawable.sofasatu),
        ShopProduct("Sofa Minimalis Nordic", "Sofa", 2450000L, "4.8", "98", R.drawable.sofadua),
        ShopProduct("Sofa L-Shape Keluarga", "Sofa", 4200000L, "4.9", "64", R.drawable.sofatiga),
        ShopProduct("Kursi Lounge Kayu Jati", "Kursi", 1275000L, "4.8", "115", R.drawable.sofadua),
        ShopProduct("Meja Tamu Oak Minimalis", "Meja", 1850000L, "4.7", "83", R.drawable.sofatiga),
        ShopProduct("Sofa Velour Emerald", "Sofa", 3350000L, "4.9", "51", R.drawable.sofasatu)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Klik Kartu Keranjang -> buka CartActivity
        binding.cardMenuCart.setOnClickListener {
            startActivity(Intent(requireContext(), CartActivity::class.java))
        }

        // 2. Klik Kartu Status Pesanan -> buka OrderStatusActivity (CRUD Orders)
        binding.cardMenuOrderStatus.setOnClickListener {
            startActivity(Intent(requireContext(), OrderStatusActivity::class.java))
        }

        // 3. Klik Banner -> buka ManageProductActivity (CRUD Products Supabase)
        binding.cardPromoBanner.setOnClickListener {
            startActivity(Intent(requireContext(), ManageProductActivity::class.java))
        }

        setupCatalogRecycler()
        setupSearchAndFilter()
        updateTopBadges()
    }

    override fun onResume() {
        super.onResume()
        updateTopBadges()
        syncDataFromSupabase()
    }

    private fun updateTopBadges() {
        val cartQty = CartManager.getTotalQuantity()
        val orderCount = OrderManager.getOrders().size

        binding.tvBadgeCartCount.text = if (cartQty > 0) "$cartQty barang" else "Kosong"
        binding.tvBadgeOrderCount.text = "$orderCount pesanan"
    }

    // Sinkronisasi produk & jumlah pesanan terbaru dari Supabase
    private fun syncDataFromSupabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Ambil produk terbaru dari tabel 'products' Supabase
                val prodRes = SupabaseClient.api.getAllProducts()
                if (prodRes.isSuccessful && !prodRes.body().isNullOrEmpty()) {
                    val cloudItems = prodRes.body()!!.mapIndexed { index, dto ->
                        val imageRes = when (index % 3) {
                            0 -> R.drawable.sofasatu
                            1 -> R.drawable.sofadua
                            else -> R.drawable.sofatiga
                        }
                        ShopProduct(
                            name = dto.name,
                            category = dto.category,
                            price = dto.price,
                            rating = "4.9",
                            soldCount = "Stok ${dto.stock}",
                            imageRes = imageRes
                        )
                    }
                    displayedProducts.clear()
                    displayedProducts.addAll(cloudItems)
                    applyFilter()
                }

                // Update badge jumlah pesanan dari tabel 'orders' Supabase
                val orderRes = SupabaseClient.api.getAllOrders()
                if (orderRes.isSuccessful) {
                    val totalCloudOrders = orderRes.body()?.size ?: 0
                    if (_binding != null) {
                        binding.tvBadgeOrderCount.text = "$totalCloudOrders pesanan"
                    }
                }
            } catch (_: Exception) {
                // Tetap gunakan data lokal jika offline
            }
        }
    }

    private fun setupCatalogRecycler() {
        adapter = ShopProductAdapter(
            products = displayedProducts,
            onProductClick = { product ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("EXTRA_PRODUCT_NAME", product.name)
                    putExtra("EXTRA_PRODUCT_PRICE", CartManager.formatRupiah(product.price))
                    putExtra("PRODUCT_NAME", product.name)
                    putExtra("PRODUCT_PRICE", CartManager.formatRupiah(product.price))
                    putExtra("PRODUCT_IMAGE", product.imageRes)
                }
                startActivity(intent)
            },
            onQuickAddClick = { product ->
                CartManager.addItem(
                    CartItem(
                        id = System.currentTimeMillis().toString(),
                        name = product.name,
                        variant = "Emerald • 3 Seater",
                        price = product.price,
                        imageRes = product.imageRes,
                        quantity = 1
                    )
                )
                updateTopBadges()
                Toast.makeText(requireContext(), "${product.name} masuk keranjang!", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvShopCatalog.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvShopCatalog.adapter = adapter
    }

    private fun setupSearchAndFilter() {
        binding.etSearchShop.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString()?.trim().orEmpty()
                applyFilter()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.chipGroupCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedCategory = when (checkedIds.firstOrNull()) {
                R.id.chipSofa -> "Sofa"
                R.id.chipKursi -> "Kursi"
                R.id.chipMeja -> "Meja"
                else -> "Semua"
            }
            applyFilter()
        }
    }

    private fun applyFilter() {
        if (_binding == null) return
        val filtered = displayedProducts.filter { item ->
            val matchCat = selectedCategory == "Semua" || item.category.equals(selectedCategory, true)
            val matchQuery = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
        binding.tvCatalogHeader.text = if (selectedCategory == "Semua") {
            "Semua Produk Tersedia (${filtered.size})"
        } else {
            "Kategori $selectedCategory (${filtered.size})"
        }
        adapter.updateList(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}