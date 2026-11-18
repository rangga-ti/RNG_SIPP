package com.example.rng_sipp.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.R

class ProductAdapter(
    private var productList: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto: ImageView = view.findViewById(R.id.ivProductPhoto)
        val tvBadge: TextView = view.findViewById(R.id.tvProductBadge)
        val tvCategory: TextView = view.findViewById(R.id.tvProductCategory)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvRating: TextView = view.findViewById(R.id.tvProductRating)
        val tvSold: TextView = view.findViewById(R.id.tvProductSold)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.ivPhoto.setImageResource(product.imageRes)
        holder.tvBadge.text = product.badge
        holder.tvCategory.text = product.category
        holder.tvName.text = product.name
        holder.tvRating.text = product.rating
        holder.tvSold.text = "• ${product.soldCount}"
        holder.tvPrice.text = product.price

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}