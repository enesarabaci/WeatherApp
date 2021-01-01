package com.example.weatherapp.Model

class Weather(
    val main: HashMap<String, Any>,
    val weather: ArrayList<HashMap<String, Any>>,
    val name: String
)