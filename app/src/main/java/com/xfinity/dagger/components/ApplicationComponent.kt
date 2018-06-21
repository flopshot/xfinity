package com.xfinity.dagger.components

import android.app.Application
import com.xfinity.CharacterViewerApplication
import com.xfinity.api.ApiModule
import com.xfinity.dagger.modules.ActivityBuilderModule
import com.xfinity.dagger.modules.ApplicationModule
import com.xfinity.data.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton



@Singleton
@Component(modules = [ApplicationModule::class, ActivityBuilderModule::class, ApiModule::class,
                       DatabaseModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun injectApplicationComponent(application: CharacterViewerApplication)
}