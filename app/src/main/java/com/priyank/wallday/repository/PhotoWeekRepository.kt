package com.priyank.wallday.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.database.ImageWeekDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PhotoWeekRepository(private val imageWeekDao: ImageWeekDao) {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun getWeeksImage(): LiveData<APIResource<List<ImageWeek>>> {
        val data = MutableLiveData<APIResource<List<ImageWeek>>>()
        data.value = APIResource.loading(null)
        val disposable = imageWeekDao.getAllWeekImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseModel ->
                if (responseModel == null) {
                    data.value = APIResource.error("", null)
                    return@subscribe
                }

                if (responseModel.isNullOrEmpty()) {
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

    fun updateImageOfDay(imageWeek: ImageWeek): LiveData<APIResource<Int>> {
        val data = MutableLiveData<APIResource<Int>>()
        data.value = APIResource.loading(null)
        val disposable = imageWeekDao.update(imageWeek)
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

    fun insertAllTheImageFirstTime(imageWeek: List<ImageWeek>): LiveData<APIResource<List<Long>>> {
        val data = MutableLiveData<APIResource<List<Long>>>()
        data.value = APIResource.loading(null)
        val disposable = imageWeekDao.insertAllTheImageWeek(*imageWeek.toTypedArray())
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
