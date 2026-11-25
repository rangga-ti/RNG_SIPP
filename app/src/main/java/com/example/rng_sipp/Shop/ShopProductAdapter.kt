package com.example.rng_sipp.Shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.databinding.ItemShopProductBinding

data class ShopProduct(
    val name: String,
    val category: String,
    val price: Long,
    val rating: String,
    val soldCount: String,
    val imageRes: Int
)

class ShopProductAdapter(
    private var products: List<ShopProduct>,
    private val onProductClick: (ShopProduct) -> Unit,
    private val onQuickAddClick: (ShopProduct) -> Unit
) : RecyclerView.Adapter<ShopProductAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(val binding: ItemShopProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ItemShopProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            ivShopProductImage.setImageResource(product.imageRes)
            tvShopProductBadge.text = product.category
            tvShopProductName.text = product.name
            tvShopProductRating.text = "★ ${product.rating} • Terjual ${product.soldCount}"
            tvShopProductPrice.text = CartManager.formatRupiah(product.price)

            root.setOnClickListener { onProductClick(product) }
            btnQuickAddCart.setOnClickListener { onQuickAddClick(product) }
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: List<ShopProduct>) {
        products = newList
        notifyDataSetChanged()
    }
}