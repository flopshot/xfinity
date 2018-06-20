package com.xfinity.repository

import com.xfinity.api.ApiResource
import com.xfinity.api.CharacterViewerApi
import com.xfinity.api.apiresponses.CharactersResponse
import com.xfinity.data.AppDatabase
import com.xfinity.data.entities.CharacterEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(val api: CharacterViewerApi, val appDatabase: AppDatabase) {

    fun getCharactersObservable(): Observable<ApiResource<List<CharacterEntity>>>  {

        return object: BaseNetworkResource<CharactersResponse, List<CharacterEntity>>(appDatabase) {

            override fun createCall(): Observable<CharactersResponse> = api.getCharacterList()
            override fun loadFromDb(): Flowable<List<CharacterEntity>> = appDatabase.characterDao().getCharactersFlowable()
            override fun shouldFetch(): Boolean = true

        }.asObservable()
    }

    fun getCharacterObservable(id: String): Observable<CharacterEntity>
            = appDatabase.characterDao().getCharacterFlowable(id).toObservable()

    fun setCharacterFavoriteStatus(character: CharacterEntity, favorite: Boolean) {
        character.isFavorite = favorite
        appDatabase.characterDao().update(character)
    }
}