package com.example.rng_sipp.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.rng_sipp.Shop.ProductDetail.DetailActivity
import com.example.rng_sipp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        // Search Button
        binding.btnSearch.setOnClickListener {
            Toast.makeText(requireContext(), "Pencarian diklik", Toast.LENGTH_SHORT).show()
        }

        // PopUp Menu Titik 3
        binding.btnMoreMenu.setOnClickListener { anchorView ->
            showOptionMenu(anchorView)
        }

        // AlertDialog Pusat Bantuan
        binding.btnQuickHelp.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Pusat Bantuan")
                .setMessage("Hubungi CS RNG Interior via email di support@rngsipp.com.")
                .setPositiveButton("Tutup") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        // INTENT: Klik Banner Hero ke Detail
        binding.cardHeroBanner.setOnClickListener {
            navigateToDetail("Sofa Ruang Tamu Skandinavia", "Rp3.000.000")
        }

        // INTENT: Klik Tombol Jelajahi di Banner
        binding.btnExploreHero.setOnClickListener {
            navigateToDetail("Set Meja Kayu Minimalis", "Rp1.850.000")
        }
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