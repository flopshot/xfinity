package com.xfinity.api

import com.google.gson.Gson
import com.xfinity.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun getApi(apiRetrofit: Retrofit): CharacterViewerApi {
        return apiRetrofit.create<CharacterViewerApi>(CharacterViewerApi::class.java)
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .build()
    }
}