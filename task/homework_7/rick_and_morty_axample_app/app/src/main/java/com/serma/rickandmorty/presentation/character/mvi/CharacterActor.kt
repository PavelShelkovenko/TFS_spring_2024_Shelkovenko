package com.serma.rickandmorty.presentation.character.mvi

import com.serma.rickandmorty.domain.RandomCharacterUseCase
import com.serma.rickandmorty.presentation.character.mapper.toPersonUi
import com.serma.rickandmorty.presentation.mvi.MviActor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterActor(private val useCase: RandomCharacterUseCase) :
    MviActor<CharacterPartialState, CharacterIntent, CharacterState, CharacterEffect>() {
    override fun resolve(
        intent: CharacterIntent,
        state: CharacterState
    ): Flow<CharacterPartialState> {
        return when (intent) {
            CharacterIntent.Init -> nextPageLoadData(0)
            CharacterIntent.NextPage -> nextPageLoadData(state.page + 1)
            CharacterIntent.PrevPage -> prevPageLoadData(state.page - 1)
            CharacterIntent.ReloadPage -> nextPageLoadData(state.page + 1)
        }
    }

    private fun nextPageLoadData(page: Int): Flow<CharacterPartialState> {
        return flow {
            runCatching {
                useCase.invoke(page)
            }.fold(
                onSuccess = {
                    emit(CharacterPartialState.NextPageLoaded(it.toPersonUi(), page))
                },
                onFailure = {
                    _effects.emit(CharacterEffect.ShowError(it))
                }
            )
        }
    }

    private fun prevPageLoadData(page: Int): Flow<CharacterPartialState> {
        return flow {
            runCatching {
                useCase.invoke(page)
            }.fold(
                onSuccess = {
                    emit(CharacterPartialState.PrevPageLoaded(it.toPersonUi(), page))
                },
                onFailure = {
                    _effects.emit(CharacterEffect.ShowError(it))
                }
            )
        }
    }
}