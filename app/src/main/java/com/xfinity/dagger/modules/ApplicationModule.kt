package com.xfinity.dagger.modules

import android.app.Application
import android.content.Context
import com.xfinity.ui.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}