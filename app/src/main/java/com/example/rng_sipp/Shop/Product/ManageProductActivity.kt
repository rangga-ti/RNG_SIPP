package com.example.rng_sipp.Shop.Product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.ProductDto
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.SupabaseClient
import com.example.rng_sipp.databinding.ActivityManageProductBinding
import com.example.rng_sipp.databinding.ItemCrudProductBinding
import kotlinx.coroutines.launch

class ManageProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageProductBinding
    private val productList = mutableListOf<ProductDto>()
    private lateinit var adapter: CrudProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackCrudProd.setOnClickListener { finish() }

        adapter = CrudProductAdapter(
            items = productList,
            onEdit = { product -> showProductFormDialog(product) },
            onDelete = { product -> confirmDeleteDialog(product) }
        )

        binding.rvCrudProducts.layoutManager = LinearLayoutManager(this)
        binding.rvCrudProducts.adapter = adapter

        binding.fabAddProduct.setOnClickListener {
            showProductFormDialog(null) // null = mode CREATE
        }

        loadProductsFromSupabase()
    }

    // READ: Ambil semua data dari tabel products Supabase
    private fun loadProductsFromSupabase() {
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.getAllProducts()
                if (response.isSuccessful) {
                    productList.clear()
                    productList.addAll(response.body().orEmpty())
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@ManageProductActivity,
                        "Gagal memuat produk (${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ManageProductActivity,
                    "Error koneksi: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // CREATE & UPDATE: Dialog Form Tambah / Edit Produk
    private fun showProductFormDialog(existing: ProductDto?) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 12)
        }

        val etName = EditText(this).apply {
            hint = "Nama Perabotan"
            setText(existing?.name.orEmpty())
        }
        val etCat = EditText(this).apply {
            hint = "Kategori (Sofa / Kursi / Meja)"
            setText(existing?.category ?: "Sofa")
        }
        val etPrice = EditText(this).apply {
            hint = "Harga (angka saja)"
            setText(existing?.price?.toString().orEmpty())
        }
        val etStock = EditText(this).apply {
            hint = "Stok"
            setText(existing?.stock?.toString() ?: "10")
        }
        val etMat = EditText(this).apply {
            hint = "Material"
            setText(existing?.material ?: "Kayu Jati")
        }

        layout.addView(etName)
        layout.addView(etCat)
        layout.addView(etPrice)
        layout.addView(etStock)
        layout.addView(etMat)

        AlertDialog.Builder(this)
            .setTitle(if (existing == null) "Tambah Produk ke Supabase" else "Edit Produk")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val dto = ProductDto(
                    name = etName.text.toString().trim().ifBlank { "Sofa Baru" },
                    category = etCat.text.toString().trim().ifBlank { "Sofa" },
                    price = etPrice.text.toString().trim().toLongOrNull() ?: 2500000L,
                    stock = etStock.text.toString().trim().toIntOrNull() ?: 10,
                    material = etMat.text.toString().trim().ifBlank { "Kayu Solid" }
                )

                lifecycleScope.launch {
                    try {
                        val res = if (existing == null) {
                            SupabaseClient.api.createProduct(dto) // CREATE
                        } else {
                            SupabaseClient.api.updateProduct("eq.${existing.id}", dto) // UPDATE
                        }
                        if (res.isSuccessful) {
                            Toast.makeText(
                                this@ManageProductActivity,
                                if (existing == null) "Produk ditambahkan!" else "Produk diperbarui!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadProductsFromSupabase()
                        } else {
                            Toast.makeText(
                                this@ManageProductActivity,
                                "Gagal menyimpan (${res.code()})",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ManageProductActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Konfirmasi sebelum DELETE
    private fun confirmDeleteDialog(product: ProductDto) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Produk")
            .setMessage("Yakin ingin menghapus \"${product.name}\" dari Supabase?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteProductFromSupabase(product)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // DELETE: Hapus produk dari Supabase
    private fun deleteProductFromSupabase(product: ProductDto) {
        val targetId = product.id ?: return
        lifecycleScope.launch {
            try {
                val res = SupabaseClient.api.deleteProduct("eq.$targetId")
                if (res.isSuccessful) {
                    Toast.makeText(
                        this@ManageProductActivity,
                        "${product.name} dihapus!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadProductsFromSupabase()
                } else {
                    Toast.makeText(
                        this@ManageProductActivity,
                        "Gagal menghapus (${res.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ManageProductActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    inner class CrudProductAdapter(
        private val items: List<ProductDto>,
        private val onEdit: (ProductDto) -> Unit,
        private val onDelete: (ProductDto) -> Unit
    ) : RecyclerView.Adapter<CrudProductAdapter.VH>() {

        inner class VH(val b: ItemCrudProductBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            VH(ItemCrudProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.b.tvCrudProdName.text = item.name
            holder.b.tvCrudProdMeta.text = "${item.category} • Stok: ${item.stock} • ${item.material}"
            holder.b.tvCrudProdPrice.text = CartManager.formatRupiah(item.price)
            holder.b.btnEditProd.setOnClickListener { onEdit(item) }
            holder.b.btnDeleteProd.setOnClickListener { onDelete(item) }
        }

        override fun getItemCount(): Int = items.size
    }
}