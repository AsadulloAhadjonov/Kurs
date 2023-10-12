package com.asadullo.valyuta.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asadullo.valyuta.R
import com.asadullo.valyuta.databinding.ActivityAboutKursBinding

class AboutKurs : AppCompatActivity() {
    private val binding by lazy { ActivityAboutKursBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val code = intent.getStringExtra("code")
        val price = intent.getStringExtra("price")
        val buy = intent.getStringExtra("buy")
        val cell = intent.getStringExtra("cell")
        val date = intent.getStringExtra("date")


        binding.titleAbout.text = title
        binding.codeAbout.text = "Kurs kodi: $code"
        binding.priceAbout.text = "Narxi: $price"
        binding.buyAbout.text = "Sotib olish narxi: $buy"
        binding.cellAbout.text = "Sotish narxi: $cell"
        binding.dateAbout.text = "Yangilangan sanasi: $date"
    }
}