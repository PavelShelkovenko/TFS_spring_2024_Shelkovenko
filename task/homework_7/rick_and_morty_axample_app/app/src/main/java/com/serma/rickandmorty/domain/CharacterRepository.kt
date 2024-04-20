package com.serma.rickandmorty.domain

import com.serma.rickandmorty.domain.model.PersonDomain

interface CharacterRepository {
    suspend fun getCharactersById(id: String, page: Int): PersonDomain
}