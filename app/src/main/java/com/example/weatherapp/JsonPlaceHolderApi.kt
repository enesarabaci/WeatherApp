package com.example.weatherapp

import com.example.weatherapp.Model.Forecast
import com.example.weatherapp.Model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceHolderApi {

    @GET("/data/2.5/weather?lang=tr")
    fun getData(
        @Query("q") name: String,
        @Query("units") units: String,
        @Query("appid") api: String
    ): Call<Weather>


    @GET("/data/2.5/forecast?lang=tr")
    fun getForecast(
        @Query("q") name: String,
        @Query("units") units: String,
        @Query("appid") api: String
    ): Call<Forecast>


    @GET("/data/2.5/weather?lang=tr")
    fun getLatLng(
        @Query("lat") lat: Double, @Query("lon") lon: Double, @Query("units") units: String,
        @Query("appid") api: String
    ): Call<Weather>


}