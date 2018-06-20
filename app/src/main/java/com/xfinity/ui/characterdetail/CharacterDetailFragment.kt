package com.xfinity.ui.characterdetail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xfinity.R
import com.xfinity.dagger.Injectable
import javax.inject.Inject

class CharacterDetailFragment: Fragment(), Injectable {

    private lateinit var characterDescription: String

    private val viewModel: CharacterDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CharacterDetailViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterDescription = arguments?.getString(CHARACTER_DESCRIPTION_KEY)
                ?: throw IllegalStateException("character id not found in fragment arguments")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //TODO
    }

    companion object {
        const val CHARACTER_DESCRIPTION_KEY = "characterId"

        operator fun invoke(characterDescription: String): CharacterDetailFragment {
            val f = CharacterDetailFragment()

            val bundle = Bundle()
            bundle.putString(CHARACTER_DESCRIPTION_KEY, characterDescription)
            f.arguments = bundle
            return f
        }
    }
}