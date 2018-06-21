package com.xfinity.ui.characterlist

import android.arch.lifecycle.MutableLiveData
import com.xfinity.api.ApiResource
import com.xfinity.data.entities.CharacterEntity
import com.xfinity.repository.CharacterRepository
import com.xfinity.ui.viewmodel.BaseViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(private val characterRepo: CharacterRepository): BaseViewModel() {

    val charactersLiveData: MutableLiveData<ApiResource<CharacterEntity>> = MutableLiveData()

    private var charactersDisposable: Disposable? = null

    init {
        initCharacters()
    }

    private fun initCharacters() {
        if (charactersDisposable != null) {
            charactersDisposable!!.dispose()
        }

        charactersDisposable = this.convertObservableToLiveData(charactersDisposable, charactersLiveData, characterRepo.getCharactersObservable())
    }

}