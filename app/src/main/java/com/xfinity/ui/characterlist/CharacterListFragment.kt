package com.xfinity.ui.characterlist

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xfinity.R
import com.xfinity.dagger.Injectable
import com.xfinity.ui.characterdetail.CharacterDetailFragment
import javax.inject.Inject

class CharacterListFragment: Fragment(), Injectable {

    private val viewModel: CharacterListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CharacterListViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO
    }

    companion object {

        operator fun invoke(): CharacterDetailFragment {
            val f = CharacterDetailFragment()

            return f
        }
    }
}