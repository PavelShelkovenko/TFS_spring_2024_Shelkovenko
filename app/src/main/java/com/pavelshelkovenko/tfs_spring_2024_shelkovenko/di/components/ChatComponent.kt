package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.ChatModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.ChatScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatFragment
import dagger.Subcomponent

@ChatScope
@Subcomponent(modules = [ChatModule::class])
interface ChatComponent {

    fun inject(chatFragment: ChatFragment)
}