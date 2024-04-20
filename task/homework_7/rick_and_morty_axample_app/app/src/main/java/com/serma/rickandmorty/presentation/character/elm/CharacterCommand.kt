package com.serma.rickandmorty.presentation.character.elm

sealed interface CharacterCommand {
    data class LoadData(val page: Int) : CharacterCommand

}