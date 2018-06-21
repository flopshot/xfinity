package com.xfinity.ui.navigation

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.xfinity.R
import com.xfinity.dagger.Injectable
import com.xfinity.repository.CharacterRepository
import com.xfinity.util.Validation
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import javax.inject.Inject

class NavigationDrawerActivity : AppCompatActivity(), HasSupportFragmentInjector,
        NavigationView.OnNavigationItemSelectedListener, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var navActivityUIController: NavActivityUIController
    @Inject
    lateinit var validation: Validation



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navigationController.getListFragment()
        } else {
            searchBar.visibility = savedInstanceState.getInt(SEARCH_FIELD_VISIBLE_KEY)
        }

        navActivityUIController.setEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                val validationMsg = validation.validSearchQueryOrErrorMsg(query)

                if (!validationMsg.isEmpty()) {
                    navActivityUIController.snackBarMessage(validationMsg)
                    return@OnEditorActionListener false
                }

                navigationController.getListFragment(CharacterRepository.FILTER_TYPE_SEARCH, query)

                return@OnEditorActionListener true
            }

            return@OnEditorActionListener false
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nav_drawer_activity_tolbar, menu)
        if (searchBar.visibility == View.VISIBLE) {
            menu.findItem(R.id.menu_search).setIcon(R.drawable.ic_cancel)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
            R.id.menu_search -> {
                if (searchBar.visibility == View.GONE) {
                    searchBar.visibility = View.VISIBLE
                    searchBar.requestFocus()
                    item.setIcon(R.drawable.ic_cancel)
                } else {
                    searchBar.text.clear()
                    searchBar.visibility = View.GONE
                    item.setIcon(R.drawable.ic_search)
                    searchBar.clearFocus()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_characters -> {
                navigationController.getListFragment(CharacterRepository.FILTER_TYPE_NONE)
            }
            R.id.menu_favorites -> {
                navigationController.getListFragment(CharacterRepository.FILTER_TYPE_FAVORITES)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(SEARCH_FIELD_VISIBLE_KEY, searchBar.visibility)

        super.onSaveInstanceState(outState)
    }

    companion object {
        const val SEARCH_FIELD_VISIBLE_KEY: String = "serachfieldhiddenkey"
    }
}
