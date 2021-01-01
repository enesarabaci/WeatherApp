package com.example.weatherapp.Util

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImage(icon: String?) {
    icon?.let {
        val url = "http://openweathermap.org/img/wn/$it@2x.png"
        Picasso.get().load(url).fit().centerCrop().into(this)
    }
}