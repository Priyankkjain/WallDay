package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.database.ImageRoomDatabase
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.repository.ImageWeekRepository

class ImageWeekViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {

    private val repository: ImageWeekRepository
    private val updateImageOfDayLiveData = MutableLiveData<ImageWeek>()
    private val insertImageOfDayFirstTimeLiveData = MutableLiveData<List<ImageWeek>>()

    val imagesWeek: LiveData<APIResource<List<ImageWeek>>>
    val updateImageOfDay: LiveData<APIResource<Int>>
    val insertAllTheWeekDataFirstTime: LiveData<APIResource<List<Long>>>

    init {
        val imageDao = ImageRoomDatabase.getDatabase(applicationContext).imageDao()
        repository = ImageWeekRepository(imageDao)
        imagesWeek = repository.getWeeksImage()
        updateImageOfDay = updateImageOfDayLiveData.switchMap {
            repository.updateImageOfDay(it)
        }
        insertAllTheWeekDataFirstTime = insertImageOfDayFirstTimeLiveData.switchMap {
            repository.insertAllTheImageFirstTime(it)
        }
    }

    fun updateImageInWeek(imageWeek: ImageWeek) {
        updateImageOfDayLiveData.postValue(imageWeek)
    }

    fun addAllTheWeekImage(imageWeekList: List<ImageWeek>) {
        insertImageOfDayFirstTimeLiveData.postValue(imageWeekList)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearRepo()
    }
}
