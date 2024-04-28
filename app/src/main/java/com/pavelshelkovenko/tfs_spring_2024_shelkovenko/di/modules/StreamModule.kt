package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules


import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.StreamScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.StreamRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.StreamActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.StreamStoreFactory
import dagger.Module
import dagger.Provides

@Module
class StreamModule {

    @StreamScope
    @Provides
    fun provideStreamActor(
        streamRepository: StreamRepository
    ): StreamActor = StreamActor(streamRepository)

    @StreamScope
    @Provides
    fun provideStreamStoreFactory(
        actor: StreamActor
    ): StreamStoreFactory = StreamStoreFactory(actor)
}