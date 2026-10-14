package com.example.rng_sipp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Setup Toolbar Halaman Beranda
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarBeranda)
        setSupportActionBar(toolbar)

        // Inisialisasi Tombol Lihat Detail Produk
        val btnToDetail = findViewById<Button>(R.id.btnToDetail)

        // Pindah ke Halaman Detail Produk
        btnToDetail.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}