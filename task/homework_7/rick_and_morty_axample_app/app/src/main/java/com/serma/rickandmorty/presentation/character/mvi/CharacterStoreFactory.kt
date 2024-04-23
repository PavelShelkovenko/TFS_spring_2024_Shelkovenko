package com.serma.rickandmorty.presentation.character.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CharacterStoreFactory(
    private val reducer: CharacterReducer,
    private val actor: CharacterActor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterStore(reducer, actor) as T
    }
}