package com.xfinity.api.apiresponses

import android.support.annotation.WorkerThread
import com.google.gson.annotations.SerializedName
import com.xfinity.data.AppDatabase

class CharactersResponse(
        @SerializedName("RelatedTopics") val characters: List<CharacterResponse>
): BaseResponse {

    @WorkerThread
    override fun saveResponseToDb(db: AppDatabase): Long {
        var saveCount = 0L
        for (character in characters) {
            saveCount += character.saveResponseToDb(db)
        }
        return saveCount
    }
}