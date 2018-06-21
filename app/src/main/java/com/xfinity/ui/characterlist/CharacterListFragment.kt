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
import com.xfinity.ui.characterdetail.CharacterDetailFragment
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filterType = arguments?.getString(CharacterListFragment.FILTER_KEY)
                ?: throw IllegalStateException("No Filter Type Specified")

        viewModel.initCharacters(filterType)
    }

    companion object {
        const val FILTER_KEY = "filterKey"

        operator fun invoke(filterType: String): CharacterDetailFragment {
            val f = CharacterDetailFragment()

            val bundle = Bundle()
            bundle.putString(CharacterListFragment.FILTER_KEY, filterType)
            f.arguments = bundle

            return f
        }
    }
}