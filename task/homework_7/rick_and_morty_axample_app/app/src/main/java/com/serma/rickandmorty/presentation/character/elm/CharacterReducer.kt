package com.serma.rickandmorty.presentation.character.elm

import com.serma.rickandmorty.presentation.utils.LceState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class CharacterReducer :
    ScreenDslReducer<CharacterEvent,
            CharacterEvent.Ui,
            CharacterEvent.Domain,
            CharacterStateElm,
            CharacterEffectElm,
            CharacterCommand>(
        CharacterEvent.Ui::class, CharacterEvent.Domain::class
    ) {

    override fun Result.internal(event: CharacterEvent.Domain) = when (event) {
        is CharacterEvent.Domain.DataLoaded -> state {
            copy(personUi = LceState.Content(event.personUi), page = event.page)
        }

        is CharacterEvent.Domain.Error -> effects {
            +CharacterEffectElm.ShowError(event.throwable)
        }
    }

    override fun Result.ui(event: CharacterEvent.Ui) = when (event) {
        CharacterEvent.Ui.Init -> commands {
            +CharacterCommand.LoadData(0)
        }

        CharacterEvent.Ui.NextPage -> commands {
            +CharacterCommand.LoadData(state.page + 1)
        }

        CharacterEvent.Ui.PrevPage -> commands {
            +CharacterCommand.LoadData(state.page - 1)
        }

        CharacterEvent.Ui.ReloadPage -> commands {
            +CharacterCommand.LoadData(state.page + 1)
        }
    }
}