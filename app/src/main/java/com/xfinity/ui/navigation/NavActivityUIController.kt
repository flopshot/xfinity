package com.xfinity.ui.navigation

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.xfinity.R
import com.xfinity.ui.characterdetail.CharacterDetailFragment
import com.xfinity.ui.characterlist.CharacterListFragment
import com.xfinity.util.Const
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import javax.inject.Inject

class NavActivityUIController @Inject constructor(private val activity: NavigationDrawerActivity) {

    private var curTextWatcher: TextWatcher? = null

    private val isTablet: Boolean by lazy {
        activity.resources.configuration.smallestScreenWidthDp >= Const.TABLET_SMALLEST_WIDTH
    }

    private val characterListTitle: String by lazy {
        activity.resources.getString(R.string.character_list_fragment_title)
    }

    fun setActionBarTitle(title: String = "", fragment: Fragment) {
        if (!isTablet) {
            if (fragment is CharacterListFragment) {
                activity.supportActionBar?.title = characterListTitle
            } else {
                activity.supportActionBar?.title = title
            }
        } else {
            if (fragment is CharacterDetailFragment) {
                activity.supportActionBar?.title = "$characterListTitle : $title"
            }
        }
    }

    fun snackBarMessage(msg: String) {
        Snackbar.make(activity.coordinatorLayout, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoadingBar() {
        activity.loadingBar.visibility = View.VISIBLE
    }

    fun hideLoadingBar() {
        activity.loadingBar.visibility = View.GONE
    }

    fun setSearchTextChangeListener(textWatcher: TextWatcher?) {
        clearSearchTextChangeListener()
        curTextWatcher = textWatcher
        activity.searchBar.addTextChangedListener(curTextWatcher)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun clearSearchTextChangeListener() {
        activity.searchBar.removeTextChangedListener(curTextWatcher)
    }

    fun setEditorActionListener(editorActionListener: TextView.OnEditorActionListener) {
        activity.searchBar.setOnEditorActionListener(editorActionListener)
    }

    fun clearEditorActionListener() {
        activity.searchBar.setOnEditorActionListener(null)
    }
}