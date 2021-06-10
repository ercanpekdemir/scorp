package com.scorp.interactor

import com.scorp.base.BaseNetworkUseCase
import com.scorp.data.FetchResponse
import com.scorp.data.NameRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetNames @Inject constructor(
    private val nameRepository: NameRepository
) : BaseNetworkUseCase<FetchResponse, GetNames.Params>() {

    override fun buildUseCaseObservable(params: Params) = nameRepository.getNames(params.next)

    data class Params(val next: String?)
}