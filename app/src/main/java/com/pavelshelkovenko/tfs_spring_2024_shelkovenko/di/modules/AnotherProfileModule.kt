package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.AnotherProfileScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another.AnotherProfileActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another.AnotherProfileStoreFactory
import dagger.Module
import dagger.Provides

@Module
class AnotherProfileModule {
    @AnotherProfileScope
    @Provides
    fun provideAnotherProfileActor(
        getAnotherProfileUseCase: GetAnotherProfileUseCase
    ): AnotherProfileActor = AnotherProfileActor(getAnotherProfileUseCase)

    @AnotherProfileScope
    @Provides
    fun provideAnotherProfileStoreFactory(
        actor: AnotherProfileActor
    ): AnotherProfileStoreFactory = AnotherProfileStoreFactory(actor)
}