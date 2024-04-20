package com.serma.rickandmorty.presentation.character.elm

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.utils.LceState

data class CharacterStateElm(val personUi: LceState<PersonUi>, val page: Int = 0)

