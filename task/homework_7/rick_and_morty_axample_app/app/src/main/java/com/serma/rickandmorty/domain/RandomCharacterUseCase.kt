package com.serma.rickandmorty.domain

import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.domain.model.PersonDomain
import kotlin.random.Random

class RandomCharacterUseCase(
    private val characterRepository: CharacterRepository
) {
    companion object {
        private const val MAX_ID = 826
    }

    suspend fun invoke(page: Int): PersonDomain {
        return characterRepository.getCharactersById(generateId(), page)
    }

    private fun generateId(): String {
       return (0..MAX_ID).random().toString()
    }
}