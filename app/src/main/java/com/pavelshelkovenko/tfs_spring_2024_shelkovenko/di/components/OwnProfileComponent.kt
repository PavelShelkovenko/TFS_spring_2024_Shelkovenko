package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.OwnProfileModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.OwnProfileScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.OwnProfileFragment
import dagger.Subcomponent

@OwnProfileScope
@Subcomponent(modules = [OwnProfileModule::class])
interface OwnProfileComponent {

    fun inject(ownProfileFragment: OwnProfileFragment)
}