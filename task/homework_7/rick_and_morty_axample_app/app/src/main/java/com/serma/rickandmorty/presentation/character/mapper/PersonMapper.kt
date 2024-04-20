package com.serma.rickandmorty.presentation.character.mapper

import com.serma.rickandmorty.data.model.CharacterStatus
import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.domain.model.PersonDomain
import com.serma.rickandmorty.presentation.character.model.CharacterStatusUi
import com.serma.rickandmorty.presentation.character.model.PersonUi

fun PersonDomain.toPersonUi(): PersonUi {
    return PersonUi(
        id = id,
        name = name,
        status = status.toCharacterStatusUi(),
        species = species,
        firstSeenIn = origin.name,
        lastLocation = location.name,
        imageUrl = image
    )
}

fun CharacterStatus.toCharacterStatusUi(): CharacterStatusUi {
    return when (this) {
        CharacterStatus.ALIVE -> CharacterStatusUi.Alive
        CharacterStatus.DEAD -> CharacterStatusUi.Dead
        CharacterStatus.UNKNOWN -> CharacterStatusUi.Unknown
    }
}