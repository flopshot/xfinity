package com.xfinity.ui.navigation

import android.support.v4.app.Fragment
import com.xfinity.R
import com.xfinity.ui.characterdetail.CharacterDetailFragment
import com.xfinity.ui.characterlist.CharacterListFragment
import com.xfinity.util.Const
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: NavigationDrawerActivity) {

    private val isTablet: Boolean by lazy {
        activity.resources.configuration.smallestScreenWidthDp >= Const.TABLET_SMALLEST_WIDTH
    }

    fun getDetailFragment(description: String) {
        val characterDetailFragment = CharacterDetailFragment(description)
        navigateToFragment(characterDetailFragment)
    }

    fun getListFragment(filterType: String = "", searchQuery: String = "") {
        val characterListFragment = CharacterListFragment(filterType, searchQuery)
        navigateToFragment(characterListFragment)
    }

    private fun navigateToFragment(fragment: Fragment) {
        if (!isTablet) {
            activity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
        } else {
            if (fragment is CharacterListFragment) {
                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container_1, fragment)
                        .addToBackStack(null)
                        .commit()
            } else if (fragment is CharacterDetailFragment) {
                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container_2, fragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }

    fun navigateToFirstCharacterInDetailFragment(description: String) {
        if (isTablet) {
            getDetailFragment(description)
        }
    }
}