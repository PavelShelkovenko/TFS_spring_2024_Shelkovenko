package com.serma.rickandmorty.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: CharacterStatus = CharacterStatus.UNKNOWN,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String?,
    @SerialName("gender") val gender: CharacterGender = CharacterGender.UNKNOWN,
    @SerialName("origin") val origin: Origin,
    @SerialName("location") val location: Location,
    @SerialName("image") val image: String,
    @SerialName("episode") val episode: List<String>,
    @SerialName("url") val url: String,
    @SerialName("created") val created: String
)

@Serializable
enum class CharacterStatus {
    @SerialName("Alive")
    ALIVE,

    @SerialName("Dead")
    DEAD,

    @SerialName("unknown")
    UNKNOWN
}

@Serializable
enum class CharacterGender {
    @SerialName("Female")
    FEMALE,

    @SerialName("Male")
    MALE,

    @SerialName("Genderless")
    GENDERLESS,

    @SerialName("unknown")
    UNKNOWN
}