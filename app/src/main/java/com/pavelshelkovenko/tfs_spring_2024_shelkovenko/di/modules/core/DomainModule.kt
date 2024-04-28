package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipStreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipUserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindUserRepository(impl: ZulipUserRepository): UserRepository

    @Binds
    fun bindStreamRepository(impl: ZulipStreamRepository): StreamRepository

    @Binds
    fun bindChatRepository(impl: ZulipChatRepository): ChatRepository
}