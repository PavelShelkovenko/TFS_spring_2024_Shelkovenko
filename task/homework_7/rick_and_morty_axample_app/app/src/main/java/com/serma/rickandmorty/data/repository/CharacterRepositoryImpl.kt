package com.serma.rickandmorty.data.repository

import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.data.model.Response
import com.serma.rickandmorty.data.source.LocalSource
import com.serma.rickandmorty.data.source.RemoteSource
import com.serma.rickandmorty.domain.CharacterRepository
import com.serma.rickandmorty.domain.mapper.toDomain
import com.serma.rickandmorty.domain.model.PersonDomain

class CharacterRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : CharacterRepository {
    override suspend fun getCharactersById(id: String, page: Int): PersonDomain {
        return (getLocal(page) ?: getRemote(id)).toDomain(page)
    }

    private suspend fun getRemote(id: String): Person {
        val person = remoteSource.getCharactersById(id)
        localSource.saveData(person)
        return person
    }

    private fun getLocal(page: Int): Person? {
        return localSource.getData(page)
    }
}