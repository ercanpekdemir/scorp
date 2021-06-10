package com.scorp.data

import io.reactivex.Single
import javax.inject.Inject

class NameDataRepository @Inject constructor(
    private val dataSource: DataSource
): NameRepository {
    override fun getNames(next: String?): Single<FetchResponse> {
        return Single.create {
            dataSource.fetch(next) { fetchResponse: FetchResponse?, fetchError: FetchError? ->
                fetchResponse?.let { success ->
                    it.onSuccess(success)
                }
                fetchError?.let { fail ->
                    it.onError(Throwable(fail.errorDescription))
                }
            }
        }
    }
}