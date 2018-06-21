package com.xfinity.ui.navigation

import android.support.v7.app.AppCompatActivity
import com.xfinity.dagger.scopes.PerActivity
import dagger.Module
import dagger.Provides

@Module
class NavDrawerActivityModule {

    @PerActivity
    @Provides
    fun getActivity(activity: NavigationDrawerActivity): AppCompatActivity {
        return activity
    }
}