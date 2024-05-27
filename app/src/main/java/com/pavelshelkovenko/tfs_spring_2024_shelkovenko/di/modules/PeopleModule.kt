package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.PeopleScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people.PeopleActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people.PeopleStoreFactory
import dagger.Module
import dagger.Provides

@Module
class PeopleModule {

    @PeopleScope
    @Provides
    fun providePeopleActor(
        userRepository: UserRepository
    ): PeopleActor = PeopleActor(userRepository)

    @PeopleScope
    @Provides
    fun providePeopleStoreFactory(
        actor: PeopleActor
    ): PeopleStoreFactory = PeopleStoreFactory(actor)
}