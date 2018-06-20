package com.xfinity.dagger.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}