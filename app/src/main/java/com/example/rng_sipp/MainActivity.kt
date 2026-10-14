package com.example.rng_sipp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Ubah teks & set navigasi ke Halaman Beranda Katalog (SecondActivity)
        binding.btnLogin.text = "MASUK KE KATALOG"
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // Ubah teks & set navigasi ke Halaman Profil Developer
        binding.btnOpenWeb.text = "PROFIL DEVELOPER"
        binding.btnOpenWeb.setOnClickListener {
            val intent = Intent(this, DeveloperProfileActivity::class.java)
            startActivity(intent)
        }
    }
}