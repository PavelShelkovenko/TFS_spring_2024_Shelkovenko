package com.serma.rickandmorty.data

import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {
    @GET("character/{id}")
    suspend fun getCharactersById(@Path("id") id: String): Person
}