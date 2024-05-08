package com.example.testapp.di

import com.example.testapp.feature_list.di.FeatureListDependencies
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ListFeatureTestDependencies : FeatureListDependencies {

    private val baseUrl: String = "http://localhost:8080"

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
