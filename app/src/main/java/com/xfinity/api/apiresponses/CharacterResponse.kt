package com.xfinity.api.apiresponses

import android.support.annotation.VisibleForTesting
import android.support.annotation.WorkerThread
import com.google.gson.annotations.SerializedName
import com.xfinity.data.AppDatabase
import com.xfinity.data.entities.CharacterEntity


data class CharacterResponse(
    @SerializedName("Icon") val icon: IconResponse,
    @SerializedName("Text") val description: String = ""
    ): BaseResponse {

    @WorkerThread
    override fun saveResponseToDb(db: AppDatabase): Long {
        val entity = characterEntityFromResponse(db)
        return db.characterDao().insert(entity)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun characterEntityFromResponse(db: AppDatabase): CharacterEntity {
        val name: String
        val endIndex = description.indexOf(" -", 0)

        name = if (endIndex == -1) {
            "No Name"
        } else {
            description.substring(0, endIndex)
        }
        val isFavorite: Boolean = db.characterDao().getIsFavorite(description)

        return CharacterEntity(description = description, pictureUrl = icon.pictureUrl,
                name = name, isFavorite = isFavorite)
    }
}