package com.hozanbaydu.customerdeatris.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hozanbaydu.customerdeatris.MySingleton
import com.hozanbaydu.customerdeatris.adapter.DeatrisAdapter
import com.hozanbaydu.customerdeatris.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.deatrisRecyclerview.layoutManager= LinearLayoutManager(this)
        val foodsAdapter= DeatrisAdapter(MySingleton.Places)
        binding.deatrisRecyclerview.adapter=foodsAdapter

        binding.backButton.setOnClickListener {
            val intent= Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}