package com.scorp.interactor

import com.scorp.MainViewModel
import com.scorp.base.BaseDisposableSingleObserver
import com.scorp.data.FetchResponse

class GetNamesObserver(private val viewModel: MainViewModel) : BaseDisposableSingleObserver<FetchResponse>(viewModel) {

    override fun onResultSuccess(result: FetchResponse) {
        viewModel.handleResponse(result)
    }

    override fun onResultFail(e: Throwable) {
        viewModel.handleError(e)
    }
}