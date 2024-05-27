package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository.ChatRepositoryImpl
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository.OwnSettingsRepositoryImpl
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository.StreamRepositoryImpl
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.repository.UserRepositoryImpl
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.ApplicationScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.OwnSettingsRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @ApplicationScope
    @Binds
    fun bindStreamRepository(impl: StreamRepositoryImpl): StreamRepository

    @ApplicationScope
    @Binds
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @ApplicationScope
    @Binds
    fun bindOwnSettingsRepository(impl: OwnSettingsRepositoryImpl): OwnSettingsRepository
}