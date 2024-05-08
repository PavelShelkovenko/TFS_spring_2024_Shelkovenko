package com.example.testapp.domain_list.model

import kotlinx.serialization.Serializable

@Serializable
data class Coffee(
    val id: Int,
    val title: String,
    val description: String
)
