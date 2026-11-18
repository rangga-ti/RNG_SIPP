package com.example.rng_sipp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rng_sipp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(2000)

            val onboardingPref = getSharedPreferences("rng_sipp_prefs", Context.MODE_PRIVATE)
            val isOnboardingDone = onboardingPref.getBoolean("is_onboarding_done", false)

            val userPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            val isLogin = userPref.getBoolean("isLogin", false)

            val intent = when {
                !isOnboardingDone -> Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
                isLogin -> Intent(this@SplashScreenActivity, BaseActivity::class.java)
                else -> Intent(this@SplashScreenActivity, AuthActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}