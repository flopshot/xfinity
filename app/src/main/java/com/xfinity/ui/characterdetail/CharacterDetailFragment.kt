package com.xfinity.ui.characterdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.xfinity.R
import com.xfinity.dagger.Injectable
import com.xfinity.ui.navigation.NavActivityUIController
import kotlinx.android.synthetic.main.fragment_character_detail.*
import javax.inject.Inject

class CharacterDetailFragment: Fragment(), Injectable {

    private lateinit var characterDescription: String

    private val viewModel: CharacterDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CharacterDetailViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var navActivityUIController: NavActivityUIController
    @Inject
    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterDescription = arguments?.getString(CHARACTER_DESCRIPTION_KEY)
                ?: throw IllegalStateException("character id not found in fragment arguments")

        viewModel.initCharacter(characterDescription)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.characterLiveData.observe(this, Observer { character ->
            if (character != null) {
                if (character.pictureUrl.isEmpty()) {
                    character_picture.setImageDrawable(ContextCompat.getDrawable(appContext, R.drawable.placeholder))
                } else {
                    character_picture.setImageDrawable(null)
                    picasso.load(character.pictureUrl)
                            .centerInside()
                            .resize(500,500)
                            .placeholder(R.drawable.placeholder)
                            .into(character_picture)
                }

                character_description.text = character.description

                navActivityUIController.setActionBarTitle(character.name, this)
            }
        })

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