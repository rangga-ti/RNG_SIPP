package com.example.rng_sipp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rng_sipp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackRegister.setOnClickListener {
            finish()
        }

        // Feature DatePicker
        binding.etTglLahir.setOnClickListener {
            showDatePicker()
        }

        // Tampilkan data yang tersimpan sebelumnya jika ada
        loadSavedData()

        binding.btnSelanjutnya.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsernameReg.text.toString().trim()
            val password = binding.etPasswordReg.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Validasi input dasar sebelum kirim ke Supabase
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama, Email, dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke SharedPreferences lokal
            saveDataToSharedPreferences()

            // Kirim (INSERT) data user ke tabel 'users' di Supabase
            registerUserToSupabase(nama, email, username, password)
        }
    }

    private fun registerUserToSupabase(
        nama: String,
        email: String,
        username: String,
        password: String
    ) {
        binding.btnSelanjutnya.isEnabled = false
        binding.btnSelanjutnya.text = "Menyimpan..."

        lifecycleScope.launch {
            try {
                val newUser = UserDto(
                    fullName = nama,
                    email = email,
                    phone = username.ifEmpty { "-" },
                    password = password
                )

                val response = SupabaseClient.api.registerUser(newUser)

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Pendaftaran berhasil tersimpan di Supabase!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@RegisterActivity, VerificationActivity::class.java)
                    startActivity(intent)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Kode ${response.code()}"
                    Toast.makeText(
                        this@RegisterActivity,
                        "Gagal daftar ke Supabase: $errorMsg",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Error koneksi: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.btnSelanjutnya.isEnabled = true
                binding.btnSelanjutnya.text = "Selanjutnya"
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etTglLahir.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveDataToSharedPreferences() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val selectedGenderId = binding.rgGender.checkedRadioButtonId
        val genderText = if (selectedGenderId != -1) {
            findViewById<RadioButton>(selectedGenderId).text.toString()
        } else ""

        editor.putString("reg_nama", binding.etNama.text.toString().trim())
        editor.putString("reg_email", binding.etEmail.text.toString().trim())
        editor.putString("reg_tgl_lahir", binding.etTglLahir.text.toString().trim())
        editor.putString("reg_gender", genderText)
        editor.putString("reg_username", binding.etUsernameReg.text.toString().trim())
        editor.putString("reg_password", binding.etPasswordReg.text.toString().trim())
        editor.putString("reg_confirm_password", binding.etConfirmPassword.text.toString().trim())

        editor.apply()
    }

    private fun loadSavedData() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        binding.etNama.setText(sharedPref.getString("reg_nama", ""))
        binding.etEmail.setText(sharedPref.getString("reg_email", ""))
        binding.etTglLahir.setText(sharedPref.getString("reg_tgl_lahir", ""))
        binding.etUsernameReg.setText(sharedPref.getString("reg_username", ""))
        binding.etPasswordReg.setText(sharedPref.getString("reg_password", ""))
        binding.etConfirmPassword.setText(sharedPref.getString("reg_confirm_password", ""))

        val savedGender = sharedPref.getString("reg_gender", "")
        if (savedGender == "Laki-laki") {
            binding.rbLaki.isChecked = true
        } else if (savedGender == "Perempuan") {
            binding.rbPerempuan.isChecked = true
        }
    }
}