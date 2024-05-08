package com.example.testapp.di

import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface NetworkModule {

    val retrofit: Retrofit
}

fun NetworkModule(): NetworkModule {
    return object : NetworkModule {

        private val baseUrl: String = "http://api.sampleapis.com"

        private val json: Json
            get() = Json { ignoreUnknownKeys = true }

        private val mediaType: MediaType
            get() = MediaType.get("application/json")

        private val jsonConverterFactory
            get() = json.asConverterFactory(mediaType)

        override val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(jsonConverterFactory)
                .build()
        }
    }
}
