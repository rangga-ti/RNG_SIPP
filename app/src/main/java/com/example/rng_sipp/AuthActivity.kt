package com.example.rng_sipp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rng_sipp.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch

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

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Username dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil data registrasi lokal dari SharedPreferences
            val savedUsername = sharedPref.getString("reg_username", "") ?: ""
            val savedPassword = sharedPref.getString("reg_password", "") ?: ""

            // Rule 1: Username == Password
            val isRule1 = inputUsername == inputPassword

            // Rule 2: Match dengan data di SharedPreferences
            val isRule2 = inputUsername == savedUsername && inputPassword == savedPassword

            if (isRule1 || isRule2) {
                loginSuccess(
                    sharedPref = sharedPref,
                    username = inputUsername,
                    fullName = sharedPref.getString("reg_nama", inputUsername) ?: inputUsername,
                    email = sharedPref.getString("reg_email", "") ?: ""
                )
            } else {
                // Rule 3: Cek akun terdaftar di tabel 'users' Supabase
                verifyUserWithSupabase(sharedPref, inputUsername, inputPassword)
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

    private fun verifyUserWithSupabase(
        sharedPref: android.content.SharedPreferences,
        inputUser: String,
        inputPass: String
    ) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "MEMERIKSA AKUN..."

        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.getUsers()
                if (response.isSuccessful) {
                    val usersList = response.body().orEmpty()

                    // Cocokkan input dengan username (kolom phone), email, atau full_name di Supabase
                    val matchedUser = usersList.find { user ->
                        (user.phone.equals(inputUser, ignoreCase = true) ||
                                user.email.equals(inputUser, ignoreCase = true) ||
                                user.fullName.equals(inputUser, ignoreCase = true)) &&
                                user.password == inputPass
                    }

                    if (matchedUser != null) {
                        loginSuccess(
                            sharedPref = sharedPref,
                            username = matchedUser.phone,
                            fullName = matchedUser.fullName,
                            email = matchedUser.email
                        )
                    } else {
                        showLoginFailedDialog()
                    }
                } else {
                    showLoginFailedDialog()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@AuthActivity,
                    "Error koneksi Supabase: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "LOGIN"
            }
        }
    }

    private fun loginSuccess(
        sharedPref: android.content.SharedPreferences,
        username: String,
        fullName: String,
        email: String
    ) {
        sharedPref.edit()
            .putBoolean("isLogin", true)
            .putString("username", username)
            .putString("reg_nama", fullName)
            .putString("reg_email", email)
            .apply()

        Toast.makeText(this, "Selamat datang, $fullName!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, BaseActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Gagal")
            .setMessage("Username atau Password salah! (Aturan: Username = Password ATAU gunakan akun terdaftar di Supabase)")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}