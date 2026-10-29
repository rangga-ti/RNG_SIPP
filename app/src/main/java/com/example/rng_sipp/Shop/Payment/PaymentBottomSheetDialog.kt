package com.example.rng_sipp.Shop.Payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rng_sipp.R
import com.example.rng_sipp.databinding.LayoutPaymentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheetDialog(
    private val onPaymentSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: LayoutPaymentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutPaymentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmPayment.setOnClickListener {
            val selectedMethod = when (binding.rgPaymentMethods.checkedRadioButtonId) {
                R.id.rbTransferBank -> "Transfer Bank (Virtual Account)"
                R.id.rbEWallet -> "E-Wallet (GoPay/OVO)"
                R.id.rbCOD -> "Bayar di Tempat (COD)"
                else -> ""
            }

            if (selectedMethod.isNotEmpty()) {
                onPaymentSelected(selectedMethod)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}