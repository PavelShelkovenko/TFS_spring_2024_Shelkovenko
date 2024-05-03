package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.OwnProfileScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.OwnProfileActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.OwnProfileStoreFactory
import dagger.Module
import dagger.Provides

@Module
class OwnProfileModule {

    @OwnProfileScope
    @Provides
    fun provideOwnProfileActor(
        getOwnProfileUseCase: GetOwnProfileUseCase
    ): OwnProfileActor = OwnProfileActor(getOwnProfileUseCase)

    @OwnProfileScope
    @Provides
    fun provideOwnProfileStoreFactory(
        actor: OwnProfileActor
    ): OwnProfileStoreFactory = OwnProfileStoreFactory(actor)

}