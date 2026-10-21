package com.example.rng_sipp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.rng_sipp.Home.HomeFragment
import com.example.rng_sipp.Message.MessageFragment
import com.example.rng_sipp.Notification.NotificationFragment
import com.example.rng_sipp.Profile.ProfileFragment
import com.example.rng_sipp.Shop.ShopFragment
import com.example.rng_sipp.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Status bar: transparan, ikon GELAP (di atas krem)
        // Navigation bar: transparan, ikon GELAP (di atas putih)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inset sistem diatur DI SINI SAJA, lalu di-consume agar tidak dihitung dua kali
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // Atas: root (krem) diberi padding setinggi status bar -> menyatu dengan halaman
            binding.main.setPadding(bars.left, bars.top, bars.right, 0)
            // Bawah: bottom nav (putih) diberi padding setinggi navigation bar
            binding.bottomNavView.setPadding(0, 0, 0, bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // Warna menu Bottom Navigation
        val navColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.green_primary),
                Color.parseColor("#9E9E9E")
            )
        )
        binding.bottomNavView.itemIconTintList = navColorStateList
        binding.bottomNavView.itemTextColor = navColorStateList

        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.nav_home
            replaceFragment(HomeFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { replaceFragment(HomeFragment()); true }
                R.id.nav_shop -> { replaceFragment(ShopFragment()); true }
                R.id.nav_message -> { replaceFragment(MessageFragment()); true }
                R.id.nav_notification -> { replaceFragment(NotificationFragment()); true }
                R.id.nav_profile -> { replaceFragment(ProfileFragment()); true }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}