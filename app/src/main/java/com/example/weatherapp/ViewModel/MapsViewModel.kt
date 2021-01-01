package com.example.weatherapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.JsonPlaceHolderApi
import com.example.weatherapp.Model.Weather
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsViewModel : ViewModel() {

    var error = MutableLiveData<Boolean>()
    var weather = MutableLiveData<Weather>()

    fun getData(latlng: LatLng, reply: String?, api: String) {
        reply?.let {

            val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
            val call: Call<Weather> = jsonPlaceHolderApi.getLatLng(latlng.latitude, latlng.longitude, reply, api)

            call.enqueue(object : Callback<Weather> {
                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    error.value = true
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (!response.isSuccessful) {
                        error.value = true
                    } else {
                        response.body()?.let {
                            weather.value = it
                            println("response")
                        }
                    }
                }
            })
        }


    }

}