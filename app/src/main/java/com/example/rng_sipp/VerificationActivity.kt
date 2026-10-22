package com.example.rng_sipp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // 1. Perbaikan: Mengubah getStringg menjadi getString
        val nama = sharedPref.getString("reg_nama", "") ?: ""
        val email = sharedPref.getString("reg_email", "") ?: ""
        val tglLahir = sharedPref.getString("reg_tgl_lahir", "") ?: ""
        val gender = sharedPref.getString("reg_gender", "") ?: ""
        val username = sharedPref.getString("reg_username", "") ?: ""
        val password = sharedPref.getString("reg_password", "") ?: ""
        val confirmPassword = sharedPref.getString("reg_confirm_password", "") ?: ""

        // Tampilkan semua data dari SharedPreferences
        binding.tvValNama.text = if (nama.isEmpty()) "(Kosong)" else nama
        binding.tvValEmail.text = if (email.isEmpty()) "(Kosong)" else email
        binding.tvValTglLahir.text = if (tglLahir.isEmpty()) "(Kosong)" else tglLahir
        binding.tvValGender.text = if (gender.isEmpty()) "(Kosong)" else gender
        binding.tvValUsername.text = if (username.isEmpty()) "(Kosong)" else username
        binding.tvValPassword.text = if (password.isEmpty()) "(Kosong)" else password
        binding.tvValConfirmPassword.text = if (confirmPassword.isEmpty()) "(Kosong)" else confirmPassword

        // 2. Perbaikan: Mengubah btnBack menjadi btnKembali
        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Tombol Submit untuk Melakukan Validasi Data
        binding.btnSubmit.setOnClickListener {
            val isAnyFieldEmpty = nama.isEmpty() || email.isEmpty() || tglLahir.isEmpty() ||
                    gender.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()

            if (isAnyFieldEmpty) {
                // 3. Perbaikan: Mengubah Builderr menjadi Builder
                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage("Semua inputan tidak boleh kosong! Silakan tekan tombol 'Kembali' untuk melengkapi form registrasi.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
                // 4. Perbaikan: Mengubah passwordd menjadi password
            } else if (password != confirmPassword) {
                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage("Password dan Confirm Password harus sama! Silakan perbaiki data Anda.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                // Jika lolos validasi, tampilkan informasi "Registrasi Berhasil"
                AlertDialog.Builder(this)
                    .setTitle("Registrasi Berhasil")
                    .setMessage("Akun Anda telah berhasil terdaftar! Silakan login menggunakan username dan password Anda.")
                    .setCancelable(false)
                    .setPositiveButton("Ke Halaman Login") { dialog, _ ->
                        dialog.dismiss()
                        // 5. Perbaikan: Mengubah AuthActivityy menjadi AuthActivity
                        val intent = Intent(this, AuthActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                    .show()
            }
        }
    }
}