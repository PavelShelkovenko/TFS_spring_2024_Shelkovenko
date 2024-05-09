package com.example.testapp.domain_list.repository

import com.example.testapp.domain_list.api.CoffeeApi
import com.example.testapp.domain_list.model.Coffee

interface CoffeeRepository {

    suspend fun getCoffee(category: String): List<Coffee>
}

class CoffeeRepositoryImpl(private val coffeeApi: CoffeeApi) : CoffeeRepository {

    override suspend fun getCoffee(category: String): List<Coffee> {
        return coffeeApi.getCoffee(category)
    }
}
