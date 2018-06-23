package com.xfinity.repository

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.xfinity.api.ApiResource
import com.xfinity.api.apiresponses.BaseResponse
import com.xfinity.data.AppDatabase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * This class is implemented when a data needs to be fetched over the network.
 * The class loads any cached data from the Database first with a Status of LOADING and then attempts
 * the network all. If the network call succeeds with http response code 200 or fails with any other
 * code, the Status will update to SUCCESS or FAIL, respectively. In either case, the reactive stream
 * from the local data base is still maintained and the UI can respond to changes in the model, even
 * if the network request failed.
 */
abstract class BaseNetworkResource<RawResult: BaseResponse, ResultType>
@MainThread constructor(private val appDatabase: AppDatabase) {

    private var result: Flowable<ApiResource<ResultType>>

    init {
        val source: Flowable<ApiResource<ResultType>>
        if (this.shouldFetch()) {
            source = this.createCall()
                    .doOnNext({
                        this.saveCallResult(it)
                    })
                    .flatMap { _ ->
                        loadFromDb().toObservable().map{ApiResource.success(it)}
                    }
                    .doOnError { t ->
                        this.onFetchFailed(t)
                    }
                    .onErrorResumeNext { t: Throwable ->
                        loadFromDb()
                                .toObservable()
                                .map{data ->
                                    ApiResource.error(t.message ?: "Unexpected Error", data)
                                }
                    }
                    .toFlowable(BackpressureStrategy.LATEST)
        } else {
            source = this.loadFromDb()
                    .subscribeOn(Schedulers.io())
                    .map{ApiResource.success(it)}
        }

        result = Flowable.concat(
                    initLoadDb()
                        .map { data -> ApiResource.loading(data) }
                        .take(1),
                    source)
                .subscribeOn(Schedulers.io())
    }

    fun asObservable(): Observable<ApiResource<ResultType>> {
        return result.toObservable()
    }

    protected fun onFetchFailed(t: Throwable) {
        t.printStackTrace()
    }

    @WorkerThread
    protected fun saveCallResult(resultType: RawResult) {
        resultType.saveResponseToDb(appDatabase)
    }

    @MainThread
    protected abstract fun shouldFetch(): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @MainThread
    protected abstract fun createCall(): Observable<RawResult>

    @MainThread
    protected fun initLoadDb(): Flowable<ResultType> {
        return loadFromDb()
    }
}