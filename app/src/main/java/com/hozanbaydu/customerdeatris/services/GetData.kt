package com.hozanbaydu.customerdeatris.services

class GetData {

        fun getPhotoPlace(photo_reference:String,maxWidh:Int):String{
        val url=StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidh")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyAEaIlpU6rEey-K8g0faopCqM1cp8JJ7KY")
        return url.toString()
    }

         fun getUrl(latitude: Double?, longitude: Double?, typePlace: String): String {

                val googlePlaceUrl=StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                googlePlaceUrl.append("?location=$latitude,$longitude")
                googlePlaceUrl.append("&radius=1000")
                googlePlaceUrl.append("&type=$typePlace")
                googlePlaceUrl.append("&key=AIzaSyAEaIlpU6rEey-K8g0faopCqM1cp8JJ7KY")
                return googlePlaceUrl.toString()
        }
        fun getDetailUrl(place_id:String):String{
        val url=StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?placeid=$place_id")
        url.append("&key=AIzaSyAEaIlpU6rEey-K8g0faopCqM1cp8JJ7KY")
        return url.toString()

    }


}