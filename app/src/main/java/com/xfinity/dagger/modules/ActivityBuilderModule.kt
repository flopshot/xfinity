package com.xfinity.dagger.modules

import com.xfinity.dagger.scopes.PerActivity
import com.xfinity.ui.navigation.NavDrawerActivityModule
import com.xfinity.ui.navigation.NavigationDrawerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [NDFragmentBuilderModule::class, NavDrawerActivityModule::class])
    internal abstract fun bindNavigationDrawerActivity(): NavigationDrawerActivity
}