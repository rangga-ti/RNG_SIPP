package com.example.rng_sipp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.rng_sipp.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Perbaikan: Menggunakan ID ViewBinding yang tepat (btnBackRegister)
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
            saveDataToSharedPreferences()
            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePicker() {
        // 2. Perbaikan: Memperbaiki typo kelas Calendar
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
        // 3. Perbaikan: Menggunakan Context.MODE_PRIVATE (Int)
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val selectedGenderId = binding.rgGender.checkedRadioButtonId
        val genderText = if (selectedGenderId != -1) {
            findViewById<RadioButton>(selectedGenderId).text.toString()
        } else ""

        editor.putString("reg_nama", binding.etNama.text.toString().trim())
        editor.putString("reg_email", binding.etEmail.text.toString().trim())
        editor.putString("reg_tgl_lahir", binding.etTglLahir.text.toString().trim())

        // 4. Perbaikan: Memanggil nama variabel yang benar (genderText)
        editor.putString("reg_gender", genderText)

        editor.putString("reg_username", binding.etUsernameReg.text.toString().trim())
        editor.putString("reg_password", binding.etPasswordReg.text.toString().trim())
        editor.putString("reg_confirm_password", binding.etConfirmPassword.text.toString().trim())

        editor.apply()
    }

    private fun loadSavedData() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // 5. Perbaikan: Mengembalikan pemanggilan method ke setText()
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