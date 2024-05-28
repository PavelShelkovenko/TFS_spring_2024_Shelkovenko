package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.OwnSettingsScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.OwnSettingsRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings.OwnSettingsActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings.OwnSettingsStoreFactory
import dagger.Module
import dagger.Provides

@Module
class OwnSettingsModule {

    @OwnSettingsScope
    @Provides
    fun provideOwnSettingsActor(
        ownSettingsRepository: OwnSettingsRepository
    ): OwnSettingsActor = OwnSettingsActor(ownSettingsRepository)

    @OwnSettingsScope
    @Provides
    fun provideOwnSettingsStoreFactory(
        actor: OwnSettingsActor
    ): OwnSettingsStoreFactory = OwnSettingsStoreFactory(actor)
}