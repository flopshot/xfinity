package com.xfinity.dagger.components

import com.xfinity.CharacterViewerApplication
import com.xfinity.api.NetworkModule
import com.xfinity.dagger.modules.ActivityBuilderModule
import com.xfinity.dagger.modules.ApplicationModule
import com.xfinity.dagger.modules.NDFragmentBuilderModule
import com.xfinity.data.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ActivityBuilderModule::class,
    NDFragmentBuilderModule::class, NetworkModule::class, DatabaseModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CharacterViewerApplication): Builder

        fun build(): ApplicationComponent
    }

    fun injectApplicationComponent(application: CharacterViewerApplication)
}