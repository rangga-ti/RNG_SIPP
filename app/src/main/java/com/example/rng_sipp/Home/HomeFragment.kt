package com.example.rng_sipp.Home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rng_sipp.R
import com.example.rng_sipp.Shop.ProductDetail.DetailActivity
import com.example.rng_sipp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back Button
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Search Button di Toolbar -> Fokus ke kolom pencarian
        binding.btnSearch.setOnClickListener {
            binding.etSearchHome.requestFocus()
            Toast.makeText(requireContext(), "Ketik nama produk untuk mencari", Toast.LENGTH_SHORT).show()
        }

        // PopUp Menu Titik 3
        binding.btnMoreMenu.setOnClickListener { anchorView ->
            showOptionMenu(anchorView)
        }

        // INTENT: Klik Banner Hero ke Detail
        binding.cardHeroBanner.setOnClickListener {
            navigateToDetail("Sofa Ruang Tamu Skandinavia", "Rp 3.000.000")
        }

        // INTENT: Klik Tombol Jelajahi di Banner
        binding.btnExploreHero.setOnClickListener {
            navigateToDetail("Set Meja Kayu Minimalis", "Rp 1.850.000")
        }

        // Klik Kartu Inspirasi Ruangan
        binding.cardInspiration1.setOnClickListener {
            navigateToDetail("Paket Ruang Tamu Minimalis", "Rp 4.500.000")
        }
        binding.cardInspiration2.setOnClickListener {
            navigateToDetail("Paket Ruang Keluarga Hangat", "Rp 5.200.000")
        }

        // Reset filter produk
        binding.tvSeeAllProducts.setOnClickListener {
            binding.etSearchHome.text?.clear()
            binding.tvProductSectionTitle.text = "Produk Pilihan Utama"
            productAdapter.updateList(allProducts)
        }

        // 1. Setup GridView dengan CustomAdapter (HomeGridAdapter)
        setupQuickMenuGrid()

        // 2. Setup RecyclerView Katalog Produk (ProductAdapter)
        setupProductRecyclerView()

        // 3. Live Search Filter pada RecyclerView
        setupSearchFilter()
    }

    private fun setupQuickMenuGrid() {
        val menuItems = listOf(
            QuickMenuItem(R.drawable.ic_menu_catalog, "Katalog"),
            QuickMenuItem(R.drawable.ic_menu_promo, "Promo"),
            QuickMenuItem(R.drawable.ic_menu_popular, "Populer"),
            QuickMenuItem(R.drawable.ic_menu_support, "Bantuan")
        )

        val gridAdapter = HomeGridAdapter(requireContext(), menuItems)
        binding.gridQuickMenu.adapter = gridAdapter

        binding.gridQuickMenu.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    binding.tvProductSectionTitle.text = "Semua Katalog Produk"
                    productAdapter.updateList(allProducts)
                    binding.nestedScrollHome.smoothScrollTo(0, binding.rvProductsHome.top)
                }
                1 -> {
                    binding.tvProductSectionTitle.text = "Produk Sedang Promo"
                    productAdapter.updateList(allProducts.filter { it.isPromo })
                    binding.nestedScrollHome.smoothScrollTo(0, binding.rvProductsHome.top)
                }
                2 -> {
                    binding.tvProductSectionTitle.text = "Produk Paling Populer"
                    productAdapter.updateList(allProducts.filter { it.isPopular })
                    binding.nestedScrollHome.smoothScrollTo(0, binding.rvProductsHome.top)
                }
                3 -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pusat Bantuan RNG Interior")
                        .setMessage("Butuh konsultasi desain atau bantuan pesanan? Hubungi CS kami di support@rngsipp.com.")
                        .setPositiveButton("Tutup") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
    }

    private fun setupProductRecyclerView() {
        allProducts = listOf(
            Product(
                name = "Sofa Minimalis Nordik 3 Seater",
                category = "Ruang Tamu",
                price = "Rp 2.450.000",
                rating = "4.9",
                soldCount = "Terjual 140+",
                badge = "Best Seller",
                imageRes = R.drawable.sofasatu,
                isPromo = false,
                isPopular = true
            ),
            Product(
                name = "Set Sofa L-Shape Modern",
                category = "Ruang Keluarga",
                price = "Rp 3.850.000",
                rating = "4.8",
                soldCount = "Terjual 95+",
                badge = "Promo 15%",
                imageRes = R.drawable.sofadua,
                isPromo = true,
                isPopular = true
            ),
            Product(
                name = "Kursi Lounge Velvet Premium",
                category = "Interior",
                price = "Rp 1.620.000",
                rating = "4.9",
                soldCount = "Terjual 82+",
                badge = "Favorit",
                imageRes = R.drawable.sofatiga,
                isPromo = false,
                isPopular = true
            ),
            Product(
                name = "Sofa Kayu Jati Skandinavia",
                category = "Ruang Tamu",
                price = "Rp 2.950.000",
                rating = "4.7",
                soldCount = "Terjual 64+",
                badge = "Promo 10%",
                imageRes = R.drawable.sofasatu,
                isPromo = true,
                isPopular = false
            ),
            Product(
                name = "Sofa Santai Cozy Minimalis",
                category = "Kamar & Lounge",
                price = "Rp 2.150.000",
                rating = "4.8",
                soldCount = "Terjual 110+",
                badge = "New",
                imageRes = R.drawable.sofadua,
                isPromo = false,
                isPopular = true
            ),
            Product(
                name = "Paket Ruang Tamu Estetik",
                category = "Bundling Hemat",
                price = "Rp 4.320.000",
                rating = "5.0",
                soldCount = "Terjual 50+",
                badge = "Hemat 20%",
                imageRes = R.drawable.sofatiga,
                isPromo = true,
                isPopular = false
            )
        )

        productAdapter = ProductAdapter(allProducts) { selectedProduct ->
            navigateToDetail(selectedProduct.name, selectedProduct.price)
        }

        binding.rvProductsHome.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProductsHome.adapter = productAdapter
    }

    private fun setupSearchFilter() {
        binding.etSearchHome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                if (query.isEmpty()) {
                    binding.tvProductSectionTitle.text = "Produk Pilihan Utama"
                    productAdapter.updateList(allProducts)
                } else {
                    binding.tvProductSectionTitle.text = "Hasil Pencarian \"$s\""
                    val filtered = allProducts.filter {
                        it.name.lowercase().contains(query) || it.category.lowercase().contains(query)
                    }
                    productAdapter.updateList(filtered)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun navigateToDetail(productName: String, productPrice: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EXTRA_PRODUCT_NAME", productName)
            putExtra("EXTRA_PRODUCT_PRICE", productPrice)
        }
        startActivity(intent)
    }

    private fun showOptionMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add(0, 1, 0, "Notifikasi")
        popup.menu.add(0, 2, 1, "Pengaturan")
        popup.menu.add(0, 3, 2, "Tentang Aplikasi")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> {
                    Toast.makeText(requireContext(), "Menu Notifikasi", Toast.LENGTH_SHORT).show()
                    true
                }
                2 -> {
                    Toast.makeText(requireContext(), "Menu Pengaturan", Toast.LENGTH_SHORT).show()
                    true
                }
                3 -> {
                    Toast.makeText(requireContext(), "RNG Interior v1.0", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}