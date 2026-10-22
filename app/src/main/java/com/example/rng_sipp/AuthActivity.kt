package com.example.rng_sipp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val inputUsername = binding.etUsername.text.toString().trim()
            val inputPassword = binding.etPassword.text.toString().trim()

            // Ambil data registrasi dari SharedPreferences
            val savedUsername = sharedPref.getString("reg_username", "") ?: ""
            val savedPassword = sharedPref.getString("reg_password", "") ?: ""

            // Rule 1: Username == Password
            val isRule1 = inputUsername.isNotEmpty() && inputUsername == inputPassword

            // Rule 2: Match dengan data di SharedPreferences
            val isRule2 = inputUsername.isNotEmpty() &&
                    inputUsername == savedUsername &&
                    inputPassword == savedPassword

            if (isRule1 || isRule2) {
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", inputUsername)
                editor.apply()

                val intent = Intent(this, BaseActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username atau Password salah! (Aturan: Username = Password ATAU gunakan akun terdaftar)")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        // Navigasi ke Halaman Registrasi
        binding.btnRegisterLink?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnOpenWeb.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ikea.co.id"))
            startActivity(webIntent)
        }
    }
}