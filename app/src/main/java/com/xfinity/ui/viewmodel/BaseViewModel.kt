package com.xfinity.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    fun <T> convertObservableToLiveData(disposable: Disposable?, mutableLiveData: MutableLiveData<T>,
                                        observable: Observable<*>): Disposable {
        if (disposable != null) {
            disposable.dispose()
            disposables.remove(disposable)
        }

        @Suppress("UNCHECKED_CAST")
        val returnDisposable
                = observable
                    .subscribe({ mutableLiveData.postValue(it as T) },
                               { t -> throw Throwable(t as Throwable) }
                    )

        disposables.add(returnDisposable)
        return returnDisposable
    }

    override fun onCleared() {
        disposables.clear()
    }
}
