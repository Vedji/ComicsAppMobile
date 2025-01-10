package com.example.comicsapp.utils.network

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
object GsonProvider {
    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(json.asString)
            })
            .create()
    }
}