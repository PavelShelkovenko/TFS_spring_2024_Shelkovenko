package com.example.testapp.domain_list.api

import com.example.testapp.domain_list.model.Coffee
import retrofit2.http.GET
import retrofit2.http.Path

interface CoffeeApi {

    @GET("coffee/{category}")
    suspend fun getCoffee(@Path("category") category: String): List<Coffee>
}
