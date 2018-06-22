package com.xfinity.ui.navigation

import android.content.Context
import android.support.design.widget.Snackbar
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.xfinity.R
import com.xfinity.R.id.menu_search
import com.xfinity.dagger.scopes.PerActivity
import com.xfinity.ui.characterdetail.CharacterDetailFragment
import com.xfinity.ui.characterlist.CharacterListFragment
import com.xfinity.util.Constants
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import javax.inject.Inject

@PerActivity
class NavActivityUIController @Inject constructor(private val activity: NavigationDrawerActivity) {

    private var curTextWatcher: TextWatcher? = null

    val isTablet: Boolean by lazy {
        activity.resources.configuration.smallestScreenWidthDp >= Constants.TABLET_SMALLEST_WIDTH
    }

    private var curCharacterListTitle: String = ""

    fun setActionBarTitle(title: String = "", tag: String) {
        if (!isTablet) {
            if (tag == CharacterListFragment.TAG) {
                curCharacterListTitle = title
                activity.supportActionBar?.title = curCharacterListTitle
            } else if (tag == CharacterDetailFragment.TAG) {
                activity.supportActionBar?.title = title
            }
        } else {
            if (tag == CharacterDetailFragment.TAG) {
                if (curCharacterListTitle.isEmpty()) throw IllegalArgumentException("Character List Title Is Empty")
                activity.supportActionBar?.title = "$curCharacterListTitle : $title"
            } else if (tag == CharacterListFragment.TAG) {
                curCharacterListTitle = title
                activity.supportActionBar?.title = curCharacterListTitle
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

    fun clearSearchBarFocus() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.searchBar.applicationWindowToken, 0)
        activity.searchBar.visibility = View.GONE
        activity.toolbar.menu.findItem(menu_search).setIcon(R.drawable.ic_search)
    }

}