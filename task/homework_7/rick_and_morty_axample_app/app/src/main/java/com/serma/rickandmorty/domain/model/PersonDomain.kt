package com.serma.rickandmorty.domain.model

import com.serma.rickandmorty.data.model.CharacterGender
import com.serma.rickandmorty.data.model.CharacterStatus
import com.serma.rickandmorty.data.model.Location
import com.serma.rickandmorty.data.model.Origin

data class PersonDomain(
    val id: Int,
    val page: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String?,
    val gender: CharacterGender,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)