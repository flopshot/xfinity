package com.xfinity.api.apiresponses

import android.support.annotation.WorkerThread
import com.google.gson.annotations.SerializedName
import com.xfinity.data.AppDatabase
import com.xfinity.data.entities.CharacterEntity


data class CharacterResponse(
    @SerializedName("Icon") val icon: IconResponse,
    @SerializedName("Text") val description: String
    ): BaseResponse {

    @WorkerThread
    override fun saveResponseToDb(db: AppDatabase) {
        val name = description.substring(0, description.indexOf(" -", 0))
        val isFavorite: Boolean = db.characterDao().getIsFavorite(description)

        val entity = CharacterEntity(description = description, pictureUrl = icon.pictureUrl,
                name = name, isFavorite = isFavorite)

        db.characterDao().insert(entity)

    }
}