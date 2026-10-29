package com.example.rng_sipp.Shop.ProductDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rng_sipp.databinding.ItemSliderImageBinding

class ImageSliderAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding: ItemSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.binding.imgProductSlider.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}