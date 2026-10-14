package com.example.rng_sipp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnOpenWeb = findViewById<Button>(R.id.btnOpenWeb)

        // Pindah ke Beranda / SecondActivity
        btnLogin.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // Buka Halaman Web yang berhubungan dengan topik Perabotan
        btnOpenWeb.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ikea.co.id"))
            startActivity(webIntent)
        }
    }
}