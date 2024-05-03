package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.PeopleModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.PeopleScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people.PeopleFragment
import dagger.Subcomponent

@PeopleScope
@Subcomponent(modules = [PeopleModule::class])
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)
}