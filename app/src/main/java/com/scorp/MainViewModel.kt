package com.scorp

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.scorp.base.BaseViewModel
import com.scorp.data.FetchResponse
import com.scorp.data.Person
import com.scorp.interactor.GetNames
import com.scorp.interactor.GetNamesObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNames: GetNames
): BaseViewModel() {

    companion object {
        private const val THROTTLE_DURATION = 200L
        private const val THROTTLE_DURATION_SILENT = 50L
    }

    val namesLiveData = MutableLiveData<FetchResponse>()
    val namesErrorLiveData = MutableLiveData<String>()
    val progressBarObservable = ObservableField(View.GONE)
    val loadingObservable = ObservableField(View.GONE)
    val emptyListObservable = ObservableField(View.GONE)
    val peopleMap = hashMapOf<Int, Person>()
    var next: String? = null

    private val subjectNames = PublishSubject.create<Pair<Unit, String?>>()
    private val subjectNamesSilent = PublishSubject.create<Pair<Unit, String?>>()

    init {
        addDisposable(
            subjectNames
            .throttleFirst(THROTTLE_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                getNames.execute(GetNamesObserver(this), GetNames.Params(it.second))
            }
        )
        addDisposable(
            subjectNamesSilent
                .throttleFirst(THROTTLE_DURATION_SILENT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    getNames.executeSilently(GetNamesObserver(this), GetNames.Params(it.second))
                }
        )
    }

    fun getNames(next: String?) {
        subjectNames.onNext(Pair(Unit,next))
    }

    fun getNamesSilently(next: String?) {
        subjectNamesSilent.onNext(Pair(Unit,next))
    }

    fun handleResponse(result: FetchResponse) {
        namesLiveData.value = result
    }

    fun handleError(e: Throwable) {
        namesErrorLiveData.value = e.message
    }
}