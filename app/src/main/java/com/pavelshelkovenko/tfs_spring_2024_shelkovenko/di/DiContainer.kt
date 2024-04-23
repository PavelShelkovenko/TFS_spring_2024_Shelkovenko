package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipStreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipUserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamStoreFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.ChatActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.ChatStoreFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people.PeopleActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people.PeopleStoreFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another.AnotherProfileActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another.AnotherProfileStoreFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.own.OwnProfileActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.own.OwnProfileStoreFactory
/*
 Пока что замена Di
 */
object DiContainer {

    private val zulipApi by lazy {
        ZulipApi()
    }

    private val userRepository by lazy {
        ZulipUserRepository(zulipApi)
    }

    private val streamRepository by lazy {
        ZulipStreamRepository(zulipApi)
    }

    private val chatRepository by lazy {
        ZulipChatRepository(zulipApi)
    }

    private val getOwnProfileUseCase by lazy {
        GetOwnProfileUseCase(userRepository)
    }

    private val getAnotherProfileUseCase by lazy {
        GetAnotherProfileUseCase(userRepository)
    }

    private val ownProfileActor by lazy {
        OwnProfileActor(getOwnProfileUseCase)
    }

    private val anotherProfileActor by lazy {
        AnotherProfileActor(getAnotherProfileUseCase)
    }

    private val peopleActor by lazy {
        PeopleActor(userRepository)
    }

    private val streamActor by lazy {
        StreamActor(streamRepository)
    }

    private val chatActor by lazy {
        ChatActor(chatRepository)
    }

    val ownProfileStoreFactory by lazy {
        OwnProfileStoreFactory(ownProfileActor)
    }

    val anotherProfileStoreFactory by lazy {
        AnotherProfileStoreFactory(anotherProfileActor)
    }

    val peopleStoreFactory by lazy {
        PeopleStoreFactory(peopleActor)
    }

    val streamStoreFactory by lazy {
        StreamStoreFactory(streamActor)
    }

    val chatStoreFactory by lazy {
        ChatStoreFactory(chatActor)
    }
}