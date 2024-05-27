package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.OwnSettingsModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.OwnSettingsScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings.OwnSettingsFragment
import dagger.Subcomponent

@OwnSettingsScope
@Subcomponent(modules = [OwnSettingsModule::class])
interface OwnSettingsComponent {

    fun inject(ownSettingsFragment: OwnSettingsFragment)
}