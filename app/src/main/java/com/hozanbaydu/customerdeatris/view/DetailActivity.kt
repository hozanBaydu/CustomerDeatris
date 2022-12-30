package com.hozanbaydu.customerdeatris.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.hozanbaydu.customerdeatris.databinding.ActivityDetailBinding
import com.hozanbaydu.customerdeatris.services.GetData
import com.hozanbaydu.customerdeatris.util.SelectedPlace


class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val getPhoto= GetData()
        Glide.with(this).load(getPhoto.getPhotoPlace(SelectedPlace.selectedPlaces.reference,900)).into(binding.imageView2)
        binding.nameText.text=SelectedPlace.selectedPlaces.name
        binding.adressText.text=SelectedPlace.selectedPlaces.adress
        binding.ratingBar.rating=SelectedPlace.selectedPlaces.rating.toFloat()
    }
}
