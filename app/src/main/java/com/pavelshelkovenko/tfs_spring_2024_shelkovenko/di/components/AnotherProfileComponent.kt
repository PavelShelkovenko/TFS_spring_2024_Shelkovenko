package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.AnotherProfileModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.AnotherProfileScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another.AnotherProfileFragment
import dagger.Subcomponent

@AnotherProfileScope
@Subcomponent(modules = [AnotherProfileModule::class])
interface AnotherProfileComponent {

    fun inject(anotherProfileFragment: AnotherProfileFragment)
}