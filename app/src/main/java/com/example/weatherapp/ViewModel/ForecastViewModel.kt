package com.example.weatherapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.JsonPlaceHolderApi
import com.example.weatherapp.Model.Forecast
import com.example.weatherapp.Model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastViewModel : ViewModel() {

    var error = MutableLiveData<Boolean>()
    var weather = MutableLiveData<Weather>()
    var forecast = MutableLiveData<Forecast>()

    fun getData(name: String, reply: String?, api: String) {
        var units = ""
        reply?.let {
            if (reply == "celsius") {
                units = "metric"
            } else if (reply == "kelvin") {
                units = "standard"
            }

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

            val call: Call<Weather> = jsonPlaceHolderApi.getData(name, units, api)
            val call2: Call<Forecast> = jsonPlaceHolderApi.getForecast(name, units, api)

            call.enqueue(object : Callback<Weather> {
                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    error.value = true
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (!response.isSuccessful) {
                        error.value = true
                    } else {
                        response.body()?.let { weatherResponse ->
                            weather.value = weatherResponse
                        }
                    }
                }
            })

            call2.enqueue(object : Callback<Forecast> {
                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    error.value = true
                }

                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    if (!response.isSuccessful) {
                        error.value = true
                    } else {
                        response.body()?.let {
                            forecast.value = it
                        }
                    }
                }
            })
        }
    }
}