package com.priyank.wallday.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.priyank.wallday.api.ApiHelperClass
import com.priyank.wallday.api.requestmodel.ImageListRequestModel
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.base.APIResource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

object ImageListRepository {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun callPhotoListAPI(
        clientID: String,
        imageListRequestModel: ImageListRequestModel
    ): LiveData<APIResource<List<PhotoItem>>> {
        val data = MutableLiveData<APIResource<List<PhotoItem>>>()
        data.value = APIResource.loading(null)
        val disposable =
            ApiHelperClass.getAPIClient().callPhotosAPI(
                clientID, imageListRequestModel.page, imageListRequestModel.perPage
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseModel ->
                    if (responseModel == null) {
                        data.value = APIResource.error("", null)
                        return@subscribe
                    }

                    if (!responseModel.isNullOrEmpty()) {
                        data.value = APIResource.success(responseModel, "")
                    } else {
                        data.value = APIResource.error("", null)
                    }

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
