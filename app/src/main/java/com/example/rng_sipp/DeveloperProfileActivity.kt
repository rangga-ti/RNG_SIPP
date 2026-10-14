package com.example.rng_sipp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityDeveloperProfileBinding

class DeveloperProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeveloperProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeveloperProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbarProfile)
        binding.toolbarProfile.setNavigationOnClickListener {
            finish()
        }
    }
}