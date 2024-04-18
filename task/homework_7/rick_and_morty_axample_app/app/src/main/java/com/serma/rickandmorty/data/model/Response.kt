package com.serma.rickandmorty.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response (
    @SerialName("info")
    val info: PageInfo,
    @SerialName("results")
    val results: List<Person>,
)