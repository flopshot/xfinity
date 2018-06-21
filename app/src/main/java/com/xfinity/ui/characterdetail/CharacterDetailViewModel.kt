package com.xfinity.ui.characterdetail

import android.arch.lifecycle.MutableLiveData
import com.xfinity.api.ApiResource
import com.xfinity.data.entities.CharacterEntity
import com.xfinity.repository.CharacterRepository
import com.xfinity.ui.viewmodel.BaseViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(private val characterRepo: CharacterRepository): BaseViewModel() {

    val characterLiveData: MutableLiveData<CharacterEntity> = MutableLiveData()
    val favoriteLiveData: MutableLiveData<ApiResource<CharacterEntity>> = MutableLiveData()

    private var characterDisposable: Disposable? = null
    private var favoriteDisposable: Disposable? = null

    fun initCharacter(id: String) {
        if (characterDisposable != null) {
            characterDisposable!!.dispose()
        }

        characterDisposable = this.convertObservableToLiveData(characterDisposable, characterLiveData, characterRepo.getCharacterObservable(id))
    }

    fun favoriteButtonToggle(character: CharacterEntity) {
        if (favoriteDisposable != null) {
            favoriteDisposable!!.dispose()
        }

        favoriteDisposable = this.convertObservableToLiveData(favoriteDisposable, favoriteLiveData
                , characterRepo.setCharacterFavoriteStatus(character, !character.isFavorite))
    }

}