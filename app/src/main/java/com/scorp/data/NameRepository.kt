package com.scorp.data

import io.reactivex.Single

interface NameRepository {
    fun getNames(next: String?): Single<FetchResponse>
}