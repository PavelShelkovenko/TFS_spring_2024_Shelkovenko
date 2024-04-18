package com.serma.rickandmorty.presentation.character.elm

import com.serma.rickandmorty.domain.RandomCharacterUseCase
import com.serma.rickandmorty.presentation.character.mapper.toPersonUi
import com.serma.rickandmorty.presentation.character.mvi.CharacterEffect
import com.serma.rickandmorty.presentation.character.mvi.CharacterPartialState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class CharacterActorElm(private val useCase: RandomCharacterUseCase) :
    Actor<CharacterCommand, CharacterEvent>() {
    override fun execute(command: CharacterCommand): Flow<CharacterEvent> {
       return when (command) {
            is CharacterCommand.LoadData -> flow {
                runCatching {
                    useCase.invoke(command.page)
                }.fold(
                    onSuccess = {
                        emit(CharacterEvent.Domain.DataLoaded(it.toPersonUi(), command.page))
                    },
                    onFailure = {
                        emit(CharacterEvent.Domain.Error(it))
                    }
                )
            }
        }
    }
}