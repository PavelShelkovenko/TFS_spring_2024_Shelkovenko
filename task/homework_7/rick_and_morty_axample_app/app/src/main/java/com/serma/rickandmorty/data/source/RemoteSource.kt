package com.serma.rickandmorty.data.source

import com.serma.rickandmorty.data.RickAndMortyApi
import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.data.model.Response

class RemoteSource(private val api: RickAndMortyApi) {
   suspend fun getCharactersById(id: String): Person {
       return api.getCharactersById(id)
   }
}