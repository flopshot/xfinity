package com.xfinity.ui.characterlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xfinity.R
import com.xfinity.api.Status
import com.xfinity.dagger.Injectable
import com.xfinity.repository.CharacterRepository
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
    @Inject
    lateinit var appCxt: Context

    lateinit var filterType: String
    lateinit var searchQuery: String
    lateinit var title: String

    private var firstCharacter: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        characterRecyclerView.adapter = characterListAdapter
        characterListAdapter.selectedPosition = savedInstanceState?.getInt(SELECTED_ITEM_KEY)?:0

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

        when (filterType) {

            CharacterRepository.FILTER_TYPE_NONE -> {
                title = appCxt.getString(R.string.character_list_fragment_title)
            }

            CharacterRepository.FILTER_TYPE_SEARCH -> {
                title = "${appCxt.getString(R.string.character_list_fragment_title_srch)} \"$searchQuery\""
            }

            CharacterRepository.FILTER_TYPE_FAVORITES -> {
                title = appCxt.getString(R.string.character_list_fragment_title_faves)
            }

            else -> {
                title = appCxt.getString(R.string.character_list_fragment_title)
            }
        }

        viewModel.charactersLiveData.observe(this, Observer { charactersResource ->
            if (charactersResource != null) {
                if (firstCharacter == null && charactersResource.data != null && savedInstanceState == null) {
                    if (charactersResource.data.isNotEmpty()) {
                        firstCharacter = charactersResource.data[0].description
                        navigationController.navigateToFirstCharacterInDetailFragment(charactersResource.data[0].description)
                    } else {
                        navigationController.navigateToFirstCharacterInDetailFragment(null)
                    }
                }


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

    override fun onStart() {
        super.onStart()
        navActivityUIController.setActionBarTitle(title, CharacterListFragment.TAG)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM_KEY, characterListAdapter.selectedPosition)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val FILTER_KEY = "filterKey"
        const val SEARCH_QUERY_KEY = "searchquerykey"
        private const val SELECTED_ITEM_KEY = "selectedItemKey"

        const val TAG = "CharacterListFragment"

        operator fun invoke(filterType: String, searchQuery: String = ""): CharacterListFragment {
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