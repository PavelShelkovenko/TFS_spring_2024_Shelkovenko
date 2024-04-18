package com.serma.rickandmorty.presentation.mvi

import kotlinx.coroutines.flow.Flow

interface MviReducer<PartialState : MviPartialState, State : MviState> {
    fun reduce(prevState: State, partialState: PartialState): State
}