package com.example.rng_sipp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarDetail)
        setSupportActionBar(toolbar)

        // Mengaktifkan Listener Tombol Kembali
        toolbar.setNavigationOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }
    }
}