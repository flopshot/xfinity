package com.xfinity.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



@Module
class NetworkModule {

    val loggingInterceptor: HttpLoggingInterceptor
        @Singleton
        @Provides
        get() {
            val hli = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.i(message) }
            )
            return hli.setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

    @Singleton
    @Provides
    fun getCacheFile(context: Context): File {
        return File(context.getCacheDir(), "okhttp-cache")
    }

    @Singleton
    @Provides
    fun getOkHttpCache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000)
    }

    @Singleton
    @Provides
    fun gson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun getOkHttpClient(cache: Cache, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun getPicasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .build()
    }
}