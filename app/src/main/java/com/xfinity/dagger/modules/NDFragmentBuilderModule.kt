package com.xfinity.dagger.modules

import com.xfinity.ui.characterdetail.CharacterDetailFragment
import com.xfinity.ui.characterlist.CharacterListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NDFragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun bindCharacterListFragment(): CharacterListFragment

    @ContributesAndroidInjector
    internal abstract fun bindCharacterDetailFragment(): CharacterDetailFragment

}