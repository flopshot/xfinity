package com.xfinity.ui.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.xfinity.R
import com.xfinity.dagger.scopes.PerActivity
import com.xfinity.repository.CharacterRepository
import com.xfinity.ui.characterdetail.CharacterDetailFragment
import com.xfinity.ui.characterlist.CharacterListFragment
import com.xfinity.util.Constants
import javax.inject.Inject

@PerActivity
class NavigationController @Inject constructor(private val activity: NavigationDrawerActivity) {

    private val isTablet: Boolean by lazy {
        activity.resources.configuration.smallestScreenWidthDp >= Constants.TABLET_SMALLEST_WIDTH
    }

    fun getDetailFragment(description: String) {
        val characterDetailFragment = CharacterDetailFragment(description)
        navigateToFragment(characterDetailFragment)
    }

    fun getListFragment(filterType: String, searchQuery: String = "") {
        val characterListFragment = CharacterListFragment(filterType, searchQuery)
        navigateToFragment(characterListFragment, filterType)
    }

    private fun navigateToFragment(fragment: Fragment, filterTypeFromCharacterListFrag: String? = null) {
        val fragmentTransaction: FragmentTransaction
        if (!isTablet) {

            fragmentTransaction = activity.supportFragmentManager.beginTransaction()

            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right)

            fragmentTransaction.replace(R.id.fragment_container, fragment)

            fragmentTransaction.addToBackStack(filterTypeFromCharacterListFrag)

            fragmentTransaction.commit()
        } else {
            if (fragment is CharacterListFragment) {

                fragmentTransaction = activity.supportFragmentManager.beginTransaction()

                fragmentTransaction.setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)

                fragmentTransaction.replace(R.id.fragment_container_1, fragment)

                fragmentTransaction.addToBackStack(filterTypeFromCharacterListFrag)

                fragmentTransaction.commit()

            } else if (fragment is CharacterDetailFragment) {
                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container_2, fragment)
                        .commit()
            }
        }
    }

    fun navigateToFirstCharacterInDetailFragment(description: String?) {
        if (isTablet) {
            if (description == null) {
                for (fragment in activity.supportFragmentManager.fragments) {
                    activity.supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
            } else {
                getDetailFragment(description)
            }
        }
    }

    fun onBackPressedAndShouldCallSuper(): Boolean {
        val callSuper: Boolean
        val fragmentStackTag = peekAtFragmentStackTag()
        if (isTablet) {
            when (fragmentStackTag) {
                CharacterRepository.FILTER_TYPE_NONE -> {
                    activity.finish()
                    callSuper = false
                }

                CharacterRepository.FILTER_TYPE_FAVORITES, CharacterRepository.FILTER_TYPE_SEARCH -> {
                    getListFragment(CharacterRepository.FILTER_TYPE_NONE)
                    callSuper = false
                }

                else -> {
                    getListFragment(CharacterRepository.FILTER_TYPE_NONE)
                    callSuper = false
                }
            }
        } else {
            when (fragmentStackTag) {
                CharacterRepository.FILTER_TYPE_NONE -> {
                    activity.finish()
                    callSuper = false
                }

                CharacterRepository.FILTER_TYPE_FAVORITES, CharacterRepository.FILTER_TYPE_SEARCH -> {
                    getListFragment(CharacterRepository.FILTER_TYPE_NONE)
                    callSuper = false
                }

                else -> {
                    callSuper = true
                }
            }
        }

        return callSuper
    }

    private fun peekAtFragmentStackTag(): String {
        val tag: String
        val index = activity.supportFragmentManager.backStackEntryCount - 1
        if (index >= 0) {
            val backEntry = activity.supportFragmentManager.getBackStackEntryAt(index)
            tag = backEntry.name?:""
        } else {
            tag = ""
        }
        return tag
    }
}