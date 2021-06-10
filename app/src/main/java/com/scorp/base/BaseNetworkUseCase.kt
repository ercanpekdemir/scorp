package com.scorp.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseNetworkUseCase <Result, Params> {

    abstract fun buildUseCaseObservable(params: Params) : Single<Result>

    fun execute(observer: BaseDisposableSingleObserver<Result>, params: Params) {
        val viewModel = observer.getViewModel()
        val progressLiveData = viewModel.progressLiveData

        viewModel.addDisposable(
            buildUseCaseObservable(params)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doAfterTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        )
    }

    fun executeSilently(observer: BaseDisposableSingleObserver<Result>, params: Params) {
        val viewModel = observer.getViewModel()
        val progressSilentLiveData = viewModel.progressSilentLiveData
        viewModel.addDisposable(
            buildUseCaseObservable(params)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { progressSilentLiveData.postValue(true) }
                .doAfterTerminate { progressSilentLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        )
    }
}