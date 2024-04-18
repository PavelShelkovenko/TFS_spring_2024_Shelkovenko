package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.mvi.MviState
import com.serma.rickandmorty.presentation.utils.LceState

data class CharacterState(val personUi: LceState<PersonUi>, val page: Int = 0) : MviState