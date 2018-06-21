package com.xfinity.util

import android.content.Context
import android.text.TextUtils
import com.xfinity.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Validation @Inject constructor(private val context: Context) {

    fun validSearchQueryOrErrorMsg(query: String): String {
        if (TextUtils.isEmpty(query.trim())) {
            return context.getString(R.string.search_field_empty)
        }

        return ""
    }
}