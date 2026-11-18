package com.example.rng_sipp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var tvSkip: TextView
    private lateinit var tabIndicator: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPagerOnboarding)
        btnNext = findViewById(R.id.btnNextOnboarding)
        tvSkip = findViewById(R.id.tvSkip)
        tabIndicator = findViewById(R.id.tabIndicator)

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.sofasatu,
                "Koleksi Sofa Minimalis",
                "Jelajahi berbagai desain sofa modern dan elegan yang cocok untuk setiap sudut ruangan Anda."
            ),
            OnboardingItem(
                R.drawable.sofadua,
                "Kenyamanan & Kualitas Terbaik",
                "Dibuat dari material rangka kokoh dan busa premium untuk kenyamanan maksimal keluarga."
            ),
            OnboardingItem(
                R.drawable.sofatiga,
                "Pesan Mudah & Pengiriman Aman",
                "Pilih sofa impian, lakukan pembayaran praktis, dan pesanan dikirim langsung ke rumah Anda."
            )
        )

        val adapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = adapter

        TabLayoutMediator(tabIndicator, viewPager) { _, _ -> }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btnNext.text = if (position == onboardingItems.lastIndex) "Mulai Sekarang" else "Lanjut"
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.lastIndex) {
                viewPager.currentItem += 1
            } else {
                finishOnboarding()
            }
        }

        tvSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val prefs = getSharedPreferences("rng_sipp_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("is_onboarding_done", true).apply()

        // Arahkan ke AuthActivity (atau MainActivity sesuai alur aplikasi Anda)
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}