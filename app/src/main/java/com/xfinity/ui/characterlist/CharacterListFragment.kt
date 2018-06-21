package com.xfinity.ui.characterlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xfinity.R
import com.xfinity.api.Status
import com.xfinity.dagger.Injectable
import com.xfinity.repository.CharacterRepository.Companion.FILTER_TYPE_SEARCH
import com.xfinity.ui.clickhandlers.CharacterIdClickListener
import com.xfinity.ui.navigation.NavActivityUIController
import com.xfinity.ui.navigation.NavigationController
import kotlinx.android.synthetic.main.fragment_character_list.*
import javax.inject.Inject

class CharacterListFragment: Fragment(), Injectable {

    private val viewModel: CharacterListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CharacterListViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navActivityUIController: NavActivityUIController
    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var characterListAdapter: CharacterListAdapter

    lateinit var filterType: String
    lateinit var searchQuery: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        characterRecyclerView.adapter = characterListAdapter

        navActivityUIController.setActionBarTitle(fragment = this)

        characterListAdapter.setCharacterClickListener(object : CharacterIdClickListener {
            override fun onClick(description: String) {
                navigationController.getDetailFragment(description)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filterType = arguments?.getString(CharacterListFragment.FILTER_KEY)
                ?: throw IllegalStateException("No Filter Type Specified")

        searchQuery = arguments?.getString(SEARCH_QUERY_KEY, "")?:""

        viewModel.charactersLiveData.observe(this, Observer { charactersResource ->
            if (charactersResource != null) {
                when (charactersResource.status) {
                    Status.LOADING -> {
                        navActivityUIController.showLoadingBar()
                        if (charactersResource.data != null) {
                            characterListAdapter.setCharacterList(ArrayList(charactersResource.data))
                        }
                    }
                    Status.SUCCESS -> {
                        navActivityUIController.hideLoadingBar()
                        if (charactersResource.data != null) {
                            characterListAdapter.setCharacterList(ArrayList(charactersResource.data))
                        }
                    }
                    Status.ERROR -> {
                        navActivityUIController.hideLoadingBar()
                        if (charactersResource.data != null) {
                            characterListAdapter.setCharacterList(ArrayList(charactersResource.data))
                        }
                        if (charactersResource.message != null) {
                            navActivityUIController.snackBarMessage(charactersResource.message)
                        }
                    }
                }
            }
        })

        if (savedInstanceState == null) {
            viewModel.initCharacters(filterType, searchQuery)
        }
    }

    companion object {
        const val FILTER_KEY = "filterKey"
        const val SEARCH_QUERY_KEY = "searchquerykey"

        operator fun invoke(filterType: String, searchQuery: String): CharacterListFragment {
            val f = CharacterListFragment()

            val bundle = Bundle()
            bundle.putString(CharacterListFragment.FILTER_KEY, filterType)

            if (filterType == FILTER_TYPE_SEARCH) {
                bundle.putString(SEARCH_QUERY_KEY, searchQuery)
            }
            f.arguments = bundle

            return f
        }
    }
}