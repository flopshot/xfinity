package com.xfinity.api.apiresponses

import android.support.annotation.WorkerThread
import com.google.gson.annotations.SerializedName
import com.xfinity.data.AppDatabase

class IconResponse(@SerializedName("URL") val pictureUrl: String): BaseResponse {

    @WorkerThread
    override fun saveResponseToDb(db: AppDatabase) {}

}