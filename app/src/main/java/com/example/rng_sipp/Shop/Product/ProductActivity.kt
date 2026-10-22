package com.example.rng_sipp.Shop.Product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.R
import com.example.rng_sipp.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackProduct.setOnClickListener {
            finish()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.productFragmentContainer, ProductFragment())
                .commit()
        }
    }
}