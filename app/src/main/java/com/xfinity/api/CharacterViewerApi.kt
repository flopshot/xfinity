package com.xfinity.api

import com.xfinity.BuildConfig
import com.xfinity.api.apiresponses.CharactersResponse
import io.reactivex.Observable
import retrofit2.http.GET


interface CharacterViewerApi {

    @GET("?q=" + BuildConfig.PRODUCT_QUERY_PARAM + "&format=json")
    fun getCharacterList(): Observable<CharactersResponse>
}