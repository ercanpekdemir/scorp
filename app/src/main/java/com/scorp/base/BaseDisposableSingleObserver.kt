package com.scorp.base

import io.reactivex.observers.DisposableSingleObserver

abstract class BaseDisposableSingleObserver<Result> (private val viewModel: BaseViewModel) :
    DisposableSingleObserver<Result>() {

    override fun onSuccess(result: Result) {
        onResultSuccess(result)
    }

    override fun onError(e: Throwable) {
        onResultFail(e)
    }
    abstract fun onResultSuccess(result: Result)

    abstract fun onResultFail(e: Throwable)

    fun getViewModel() = viewModel
}