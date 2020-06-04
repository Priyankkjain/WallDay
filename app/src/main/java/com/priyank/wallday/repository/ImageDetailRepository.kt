package com.priyank.wallday.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.priyank.wallday.api.ApiHelperClass
import com.priyank.wallday.api.responsemodel.DownLoadTrackerResponse
import com.priyank.wallday.base.APIResource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

object ImageDetailRepository {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun callDownLoadTrackerAPI(
        downloadTrackerURL: String,
        clientID: String
    ): LiveData<APIResource<DownLoadTrackerResponse>> {
        val data = MutableLiveData<APIResource<DownLoadTrackerResponse>>()
        data.value = APIResource.loading(null)
        val disposable =
            ApiHelperClass.getAPIClient().callDownLoadTrackerAPI(downloadTrackerURL, clientID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseModel ->
                    if (responseModel == null) {
                        data.value = APIResource.error("", null)
                        return@subscribe
                    }
                    data.value = APIResource.success(responseModel, "")
                }, { e ->
                    Timber.e(e)
                    data.postValue(APIResource.error(e.localizedMessage ?: "", null))
                })
        compositeDisposable?.add(disposable)
        return data
    }

    fun clearRepo() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}
