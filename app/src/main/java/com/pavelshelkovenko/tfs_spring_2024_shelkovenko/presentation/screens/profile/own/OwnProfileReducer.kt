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

        is OwnProfileEvent.Internal.DataLoaded -> state {
            OwnProfileState.Content(ownUser = event.user)
        }

        is OwnProfileEvent.Internal.Error -> state {
            OwnProfileState.Error(errorMessage = event.throwable.message.toString())
        }

    }

    override fun Result.ui(event: OwnProfileEvent.Ui) = when (event) {

        is OwnProfileEvent.Ui.StartProcess -> {
            state { OwnProfileState.Loading }
            commands { +OwnProfileCommand.LoadData }
        }

        is OwnProfileEvent.Ui.ReloadData -> {
            state { OwnProfileState.Loading }
            commands { +OwnProfileCommand.LoadData }
        }

    }
}
