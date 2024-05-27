package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.components

import android.content.Context
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core.DataModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core.DomainModule
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, DomainModule::class])
interface ApplicationComponent {

    fun ownProfileComponent(): OwnProfileComponent

    fun anotherProfileComponent(): AnotherProfileComponent

    fun peopleComponent(): PeopleComponent

    fun streamComponent(): StreamComponent

    fun chatComponent(): ChatComponent

    fun ownSettingsComponent(): OwnSettingsComponent

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}