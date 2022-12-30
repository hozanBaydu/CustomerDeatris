package com.hozanbaydu.customerdeatris.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hozanbaydu.customerdeatris.databinding.RecyclerRowBinding
import com.hozanbaydu.customerdeatris.model.DataModel
import com.hozanbaydu.customerdeatris.model.MyPlaces
import com.hozanbaydu.customerdeatris.model.Results
import com.hozanbaydu.customerdeatris.services.GetData
import com.hozanbaydu.customerdeatris.util.SelectedPlace
import com.hozanbaydu.customerdeatris.view.DetailActivity

class DeatrisAdapter (val placeList:ArrayList<DataModel>): RecyclerView.Adapter<DeatrisAdapter.PlaceHolder>() {

    class PlaceHolder(val reciyclerRowBinding: RecyclerRowBinding): RecyclerView.ViewHolder(reciyclerRowBinding.root){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val reciyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceHolder(reciyclerRowBinding)
    }
    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        var getPhoto=GetData()
        holder.reciyclerRowBinding.nameText.text=placeList.get(position).name
        Glide.with(holder.reciyclerRowBinding.imageView.context).load(getPhoto.getPhotoPlace(placeList.get(position).reference,900)).into(holder.reciyclerRowBinding.imageView)
        holder.reciyclerRowBinding.adresText.text=placeList.get(position).adress
        holder.itemView.setOnClickListener {
            SelectedPlace.selectedPlaces=placeList.get(position)
            val intent= Intent(holder.itemView.context, DetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return placeList.size
    }
}