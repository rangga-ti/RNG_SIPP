package com.example.rng_sipp.Shop.Cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.databinding.ItemCartBinding

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            ivCartItemImage.setImageResource(item.imageRes)
            tvCartItemName.text = item.name
            tvCartItemVariant.text = item.variant
            tvCartItemPrice.text = CartManager.formatRupiah(item.price * item.quantity)
            tvItemQty.text = item.quantity.toString()

            btnPlusQty.setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    CartManager.updateQuantity(pos, 1)
                    notifyItemChanged(pos)
                    onCartChanged()
                }
            }

            btnMinusQty.setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    CartManager.updateQuantity(pos, -1)
                    notifyItemChanged(pos)
                    onCartChanged()
                }
            }

            btnRemoveCartItem.setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    CartManager.removeItem(pos)
                    notifyItemRemoved(pos)
                    onCartChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}