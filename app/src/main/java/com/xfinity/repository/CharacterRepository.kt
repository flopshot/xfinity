package com.xfinity.repository

import android.os.AsyncTask
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

    fun getCharactersObservable(filterType: String, searchQuery: String = ""): Observable<ApiResource<List<CharacterEntity>>>  {

        return object: BaseNetworkResource<CharactersResponse, List<CharacterEntity>>(appDatabase) {

            override fun createCall(): Observable<CharactersResponse> = api.getCharacterList()

            override fun loadFromDb(): Flowable<List<CharacterEntity>> {
                return when (filterType) {
                    FILTER_TYPE_NONE -> appDatabase.characterDao().getCharactersFlowable()
                    FILTER_TYPE_FAVORITES -> appDatabase.characterDao().getFavoriteCharactersFlowable()
                    FILTER_TYPE_SEARCH -> {
                        if (searchQuery.isEmpty()) throw IllegalArgumentException("Character name search query cannot be empty")
                        appDatabase.characterDao().getSerachCharactersFlowable(searchQuery)
                    }
                    else -> appDatabase.characterDao().getCharactersFlowable()
                }
            }

            override fun shouldFetch(): Boolean = true

        }.asObservable()
    }

    fun getCharacterObservable(id: String): Observable<CharacterEntity>
            = appDatabase.characterDao().getCharacterFlowable(id).toObservable()

    fun setCharacterFavoriteStatus(character: CharacterEntity, favorite: Boolean): Observable<ApiResource<CharacterEntity>> {
        character.isFavorite = favorite
        val characterCopy = character.copy()
        AsyncTask.execute({
            appDatabase.characterDao().update(characterCopy)
        })
        return Observable.just(ApiResource.success(character))
    }

    companion object {
        const val FILTER_TYPE_NONE = "no filter on character list"
        const val FILTER_TYPE_FAVORITES = "filter on favorite characters"
        const val FILTER_TYPE_SEARCH = "filter by search query"
    }
}