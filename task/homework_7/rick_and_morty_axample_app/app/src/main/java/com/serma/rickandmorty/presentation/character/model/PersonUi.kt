package com.serma.rickandmorty.presentation.character.model

import androidx.annotation.StringRes
import com.serma.rickandmorty.R

data class PersonUi(
    val id: Int,
    val name: String,
    val status: CharacterStatusUi,
    val species: String,
    val firstSeenIn: String,
    val lastLocation: String,
    val imageUrl: String,
)

sealed class CharacterStatusUi(@StringRes val statusRes: Int) {
    data object Alive : CharacterStatusUi(R.string.alive)
    data object Dead : CharacterStatusUi(R.string.dead)
    data object Unknown : CharacterStatusUi(R.string.unknown)
}