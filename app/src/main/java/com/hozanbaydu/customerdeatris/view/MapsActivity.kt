package com.hozanbaydu.customerdeatris.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.hozanbaydu.customerdeatris.MySingleton
import com.hozanbaydu.customerdeatris.R
import com.hozanbaydu.customerdeatris.databinding.ActivityMapsBinding
import com.hozanbaydu.customerdeatris.model.DataModel
import com.hozanbaydu.customerdeatris.model.MyPlaces
import com.hozanbaydu.customerdeatris.model.PlaceDetails
import com.hozanbaydu.customerdeatris.services.GetData
import com.hozanbaydu.customerdeatris.services.GoogleApiServices
import com.hozanbaydu.customerdeatris.util.SelectedPlace
import com.hozanbaydu.customerdeatris.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var latitude:Double?=null
    private var longitude:Double?=null
    lateinit var myService:GoogleApiServices
    internal  lateinit var currentPlace:MyPlaces

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        myService=Util.googleApiService

        binding.restaurantbutton.setOnClickListener {

            nearByPlace("restaurant")
            binding.filterLayout.visibility= View.INVISIBLE
        }

        binding.cafebutton.setOnClickListener {

            nearByPlace("cafe")
            binding.filterLayout.visibility= View.INVISIBLE
        }

        binding.filterButton.setOnClickListener {
            binding.filterLayout.visibility= View.VISIBLE
        }
    }

    private fun nearByPlace(typePlace: String) {

        MySingleton.Places.clear()
        val geturl=GetData()


        mMap.clear()
        val url=geturl.getUrl(latitude,longitude,typePlace)
        myService.getNearByPlaces(url)
            .enqueue(object :Callback<MyPlaces>{
                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    currentPlace=response.body()!!
                    if (response.isSuccessful){
                        for (i in 0 until response.body()!!.results!!.size){
                            val markerOptions=MarkerOptions()
                            val googlePlace=response.body()!!.results!![i]
                            val placeId=response.body()!!.results?.get(i)?.place_id
                            val rating=response.body()!!.results?.get(i)?.rating
                            val reference=response.body()!!.results?.get(i)?.photos?.get(0)?.photo_reference
                            val name=response.body()!!.results?.get(i)?.name
                            placeId?.let { geturl.getDetailUrl(it) }?.let {
                                myService.getDetails(it)
                                    .enqueue(object :Callback<PlaceDetails>{
                                        override fun onResponse(call: Call<PlaceDetails>, response: Response<PlaceDetails>) {
                                            val place=response.body()
                                            val adress= place?.result?.formatted_address!!

                                            val model= name?.let { it1 -> reference?.let { it2 ->
                                                rating?.let { it3 ->
                                                    DataModel(it1,
                                                        it2,adress, it3
                                                    )
                                                }
                                            } }
                                            if (model != null) {
                                                MySingleton.Places.add(model)
                                            }
                                        }
                                        override fun onFailure(call: Call<PlaceDetails>, t: Throwable) {
                                            Toast.makeText(baseContext,"Hata:"+t.message,Toast.LENGTH_SHORT).show()
                                        }

                                    })
                            }
                            val lat =googlePlace.geometry!!.location!!.lat
                            val lng =googlePlace.geometry!!.location!!.lng
                            val placeName=googlePlace.name
                            val latlng=LatLng(lat,lng)
                            markerOptions.position(latlng)
                            markerOptions.title(placeName)

                            if (typePlace.equals("cafe")){
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cafeicon))
                            }else if (typePlace.equals("restaurant")){
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restauranticon))
                            }
                            markerOptions.snippet(i.toString())
                            mMap.addMarker(markerOptions)
                        }
                    }
                }
                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    Toast.makeText(baseContext,"Hata:"+t.message,Toast.LENGTH_SHORT).show()
                }
            })
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val geturl=GetData()
        mMap = googleMap
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(binding.root, "Konum için izniniz gerekiyor", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin ver") {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        else { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0.0f, locationListener)

            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {

                val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))

                latitude=lastLocation.latitude
                longitude=lastLocation.longitude
            }
            mMap.isMyLocationEnabled=true
        }
        mMap.setOnMarkerClickListener {
            val name=currentPlace.results?.get(it.snippet.toString().toInt())?.name
            val placeId=currentPlace.results?.get(it.snippet.toString().toInt())?.place_id.toString()
            val reference=currentPlace.results?.get(it.snippet.toString().toInt())?.photos?.get(0)?.photo_reference
            val rating=currentPlace.results?.get(it.snippet.toString().toInt())?.rating

            if (rating != null) {
                SelectedPlace.selectedPlaces.rating=rating
            }

            if (name != null) {
                SelectedPlace.selectedPlaces.name=name

            }

            if (reference != null) {
                SelectedPlace.selectedPlaces.reference=reference
            }

            myService.getDetails(geturl.getDetailUrl(placeId))
                .enqueue(object :Callback<PlaceDetails>{
                    override fun onResponse(call: Call<PlaceDetails>, response: Response<PlaceDetails>) {

                        val place=response.body()
                        SelectedPlace.selectedPlaces.adress= place?.result?.formatted_address!!
                    }

                    override fun onFailure(call: Call<PlaceDetails>, t: Throwable) {
                        Toast.makeText(baseContext,"Hata:"+t.message,Toast.LENGTH_SHORT).show()
                    }
                })
            val intent= Intent(this, DetailActivity::class.java)
            startActivity(intent)
            true
        }
        binding.listButton.setOnClickListener {
            val intent= Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
        nearByPlace("restaurant") //Ekran açıldığında default gösterilecek kategori.
    }
    private fun registerLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    if (ContextCompat.checkSelfPermission(this@MapsActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, locationListener)

                        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (lastLocation != null) {
                            val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
                            latitude=lastLocation.latitude
                            longitude=lastLocation.longitude
                        }
                        mMap.isMyLocationEnabled=true
                    }
                } else {
                    Toast.makeText(this@MapsActivity, "İzin gerekiyor!", Toast.LENGTH_LONG).show()
                }
            }
    }
}


