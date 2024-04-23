package com.serma.rickandmorty.di

import com.serma.rickandmorty.data.RickAndMortyApi
import com.serma.rickandmorty.data.repository.CharacterRepositoryImpl
import com.serma.rickandmorty.data.source.LocalSource
import com.serma.rickandmorty.data.source.RemoteSource
import com.serma.rickandmorty.domain.CharacterRepository
import com.serma.rickandmorty.domain.RandomCharacterUseCase
import com.serma.rickandmorty.presentation.character.elm.CharacterActorElm
import com.serma.rickandmorty.presentation.character.elm.CharacterElmStoreFactory
import com.serma.rickandmorty.presentation.character.mvi.CharacterActor
import com.serma.rickandmorty.presentation.character.mvi.CharacterReducer
import com.serma.rickandmorty.presentation.character.mvi.CharacterStore
import com.serma.rickandmorty.presentation.character.mvi.CharacterStoreFactory
import com.serma.rickandmorty.presentation.character.mvp.CharacterPresenter
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

object DiContainer {

    val randomCharacterUseCase by lazyNone {
        RandomCharacterUseCase(repositoryCharacter)
    }

    private val repositoryCharacter: CharacterRepository by lazyNone {
        CharacterRepositoryImpl(remoteSource, localSource)
    }

    private val remoteSource by lazyNone {
        RemoteSource(api)
    }

    private val localSource by lazyNone {
        LocalSource()
    }

    val characterPresenter by lazyNone {
        CharacterPresenter(randomCharacterUseCase)
    }

    val characterStoreFactory by lazyNone {
        CharacterStoreFactory(characterReducer, characterActor)
    }

    val characterReducer by lazyNone {
        CharacterReducer()
    }

    val characterActor by lazyNone {
        CharacterActor(randomCharacterUseCase)
    }

    val characterActorElm by lazyNone {
        CharacterActorElm(randomCharacterUseCase)
    }

    val storeFactory by lazyNone {
        CharacterElmStoreFactory(characterActorElm)
    }

    private val api = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(
            Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RickAndMortyApi::class.java)
}

private fun <T> lazyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)