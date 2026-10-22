package com.example.rng_sipp.Shop.Product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rng_sipp.Shop.ProductDetail.DetailActivity
import com.example.rng_sipp.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardProduct1.setOnClickListener {
            navigateToDetail("Sofa Minimalis Nordik", "Rp 2.450.000")
        }

        binding.cardProduct2.setOnClickListener {
            navigateToDetail("Meja Kayu Jati", "Rp 850.000")
        }

        binding.cardProduct3.setOnClickListener {
            navigateToDetail("Kursi Santai Beludru", "Rp 1.120.000")
        }

        binding.cardProduct4.setOnClickListener {
            navigateToDetail("Lampu Gantung Modern", "Rp 420.000")
        }
    }

    private fun navigateToDetail(productName: String, productPrice: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EXTRA_PRODUCT_NAME", productName)
            putExtra("EXTRA_PRODUCT_PRICE", productPrice)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}