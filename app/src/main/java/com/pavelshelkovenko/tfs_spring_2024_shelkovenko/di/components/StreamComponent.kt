package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.StreamModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.StreamScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.StreamsInfoFragment
import dagger.Subcomponent

@StreamScope
@Subcomponent(modules = [StreamModule::class])
interface StreamComponent {

    fun inject(streamsInfoFragment: StreamsInfoFragment)
}