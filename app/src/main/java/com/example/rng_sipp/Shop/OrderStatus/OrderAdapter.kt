package com.example.rng_sipp.Shop.OrderStatus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.Shop.Cart.CartManager
import com.example.rng_sipp.databinding.ItemOrderStatusBinding

class OrderAdapter(
    private var list: List<OrderItem>,
    private val onOrderAction: (OrderItem) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderStatusBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderStatusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = list[position]
        with(holder.binding) {
            tvOrderId.text = order.orderId
            tvOrderStatusBadge.text = order.status
            ivOrderImage.setImageResource(order.imageRes)
            tvOrderProductName.text = order.productName
            tvOrderMeta.text = "${order.itemCount} item • ${order.paymentMethod}"
            tvOrderDate.text = "${order.date} • Cargo Heavy Delivery"
            tvOrderTotal.text = CartManager.formatRupiah(order.totalAmount)

            btnActionOrder.text = if (order.status == "Selesai") "Beli Lagi" else "Lacak & Konfirmasi"
            btnActionOrder.setOnClickListener {
                onOrderAction(order)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<OrderItem>) {
        list = newList
        notifyDataSetChanged()
    }
}