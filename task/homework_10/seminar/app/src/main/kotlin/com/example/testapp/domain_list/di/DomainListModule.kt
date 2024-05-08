package com.example.testapp.domain_list.di

import com.example.testapp.domain_list.api.CoffeeApi
import com.example.testapp.domain_list.repository.CoffeeRepository
import com.example.testapp.domain_list.repository.CoffeeRepositoryImpl

interface DomainListModule {

    val coffeeRepository: CoffeeRepository
}

fun DomainListModule(dependencies: DomainListDependencies): DomainListModule {
    return object : DomainListModule {

        private val coffeeApi: CoffeeApi by lazy {
            dependencies.retrofit.create(CoffeeApi::class.java)
        }

        override val coffeeRepository: CoffeeRepository by lazy {
            CoffeeRepositoryImpl(coffeeApi)
        }
    }
}
