package com.hozanbaydu.customerdeatris.services

import com.hozanbaydu.customerdeatris.model.MyPlaces
import com.hozanbaydu.customerdeatris.model.PlaceDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GoogleApiServices {
    @GET
    fun getNearByPlaces(@Url url:String):Call<MyPlaces>
    @GET
    fun getDetails(@Url url: String ):Call<PlaceDetails>
}