package com.xfinity.api.apiresponses

import com.xfinity.data.AppDatabase

interface BaseResponse {
    fun saveResponseToDb(db: AppDatabase)
}