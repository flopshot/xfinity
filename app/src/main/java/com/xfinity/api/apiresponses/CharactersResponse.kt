package com.xfinity.api.apiresponses

import android.support.annotation.WorkerThread
import com.google.gson.annotations.SerializedName
import com.xfinity.data.AppDatabase

class CharactersResponse(
        @SerializedName("RelatedTopics") val characters: List<CharacterResponse>
) : BaseResponse {

    @WorkerThread
    override fun saveResponseToDb(db: AppDatabase) {
        for (character in characters) {
            character.saveResponseToDb(db)
        }
    }
}