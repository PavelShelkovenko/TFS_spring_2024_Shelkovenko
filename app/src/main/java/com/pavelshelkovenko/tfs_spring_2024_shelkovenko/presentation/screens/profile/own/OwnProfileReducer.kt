package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class OwnProfileReducer : ScreenDslReducer<
        OwnProfileEvent,
        OwnProfileEvent.Ui,
        OwnProfileEvent.Internal,
        OwnProfileState,
        OwnProfileEffect,
        OwnProfileCommand>
    (OwnProfileEvent.Ui::class, OwnProfileEvent.Internal::class) {

    override fun Result.internal(event: OwnProfileEvent.Internal) = when (event) {

        is OwnProfileEvent.Internal.DataLoadedFromNetwork -> {
            state { OwnProfileState.Content(ownUser = event.user) }
        }

        is OwnProfileEvent.Internal.Error -> state { OwnProfileState.Error(errorMessage = event.errorMessage) }

        is OwnProfileEvent.Internal.DataLoadedFromCache -> {
            if (event.user == null) {
                state { OwnProfileState.Loading }
            } else {
                state { OwnProfileState.Content(ownUser = event.user) }
            }
        }

        is OwnProfileEvent.Internal.MinorError -> effects {
            +OwnProfileEffect.MinorError(errorMessageId = event.errorMessageId)
        }
    }

    override fun Result.ui(event: OwnProfileEvent.Ui) = when (event) {

        is OwnProfileEvent.Ui.StartProcess -> {
            commands { +OwnProfileCommand.LoadDataFromCache }
            commands { +OwnProfileCommand.LoadDataFromNetwork }
        }

        is OwnProfileEvent.Ui.ReloadData -> {
            state { OwnProfileState.Loading }
            commands { +OwnProfileCommand.LoadDataFromNetwork }
        }

    }
}
