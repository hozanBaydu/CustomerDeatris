package com.hozanbaydu.customerdeatris.util

import com.hozanbaydu.customerdeatris.model.Results
import com.hozanbaydu.customerdeatris.services.GoogleApiServices
import com.hozanbaydu.customerdeatris.services.RetrofitClient

object Util {

    private val GOOGLE_API_URL="https://maps.googleapis.com/"
    val googleApiService:GoogleApiServices
    get() = RetrofitClient.getClient(GOOGLE_API_URL).create(GoogleApiServices::class.java)
}