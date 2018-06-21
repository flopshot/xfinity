package com.xfinity.ui.characterdetail

import android.arch.lifecycle.LiveData
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
import com.xfinity.api.ApiResource
import com.xfinity.api.Status
import com.xfinity.dagger.Injectable
import com.xfinity.data.entities.CharacterEntity
import com.xfinity.ui.navigation.NavActivityUIController
import kotlinx.android.synthetic.main.fragment_character_detail.*
import javax.inject.Inject
import kotlin.properties.Delegates

class CharacterDetailFragment: Fragment(), Injectable {

    private var viewModel: CharacterDetailViewModel by Delegates.notNull()
    private var favoriteObserver: Observer<ApiResource<CharacterEntity>> by Delegates.notNull()
    private var favoriteLiveData: LiveData<ApiResource<CharacterEntity>> by Delegates.notNull()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var navActivityUIController: NavActivityUIController
    @Inject
    lateinit var appContext: Context

    private var character: CharacterEntity? = null
    private lateinit var characterDescription: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_character_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.characterLiveData.observe(this, Observer { character ->
            if (character != null) {
                this.character = character

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

                if (character.isFavorite) {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(appContext, R.drawable.ic_star_accent))
                } else {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(appContext, R.drawable.ic_star_hollow))
                }
            }
        })

        favoriteButton.setOnClickListener { _ ->
            if (character != null) {
                favoriteLiveData.observe(this@CharacterDetailFragment, favoriteObserver)
                viewModel.favoriteButtonToggle(character!!.copy())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterDescription = arguments?.getString(CHARACTER_DESCRIPTION_KEY)
                ?: throw IllegalStateException("character id/description not found in fragment arguments")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CharacterDetailViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.initCharacter(characterDescription)
        }

        favoriteLiveData = viewModel.favoriteLiveData
        favoriteObserver = Observer { characterResource ->
            if (characterResource != null) {
                when (characterResource.status) {
                    Status.SUCCESS -> {
                        navActivityUIController.hideLoadingBar()

                        val msg: String

                        if (characterResource.data != null) {
                            msg = if (characterResource.data.isFavorite) {
                                getString(R.string.character_fave_added)
                            } else {
                                getString(R.string.character_fave_removed)
                            }
                            navActivityUIController.snackBarMessage(msg)
                        }
                    }
                    Status.ERROR -> {
                        navActivityUIController.hideLoadingBar()
                        val msg: String
                        if (characterResource.data != null) {
                            msg = if (characterResource.data.isFavorite) {
                                getString(R.string.character_remove_error)
                            } else {
                                getString(R.string.character_add_error)
                            }
                            navActivityUIController.snackBarMessage(msg)
                        }
                    }
                    Status.LOADING -> {
                        navActivityUIController.showLoadingBar()
                    }
                }
            }
        }
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