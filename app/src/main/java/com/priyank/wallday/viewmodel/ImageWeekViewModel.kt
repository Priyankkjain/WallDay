package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.database.ImageRoomDatabase
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.repository.PhotoListRepository
import com.priyank.wallday.repository.PhotoWeekRepository

class ImageWeekViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {

    private val repository: PhotoWeekRepository
    val imagesWeek: LiveData<APIResource<List<ImageWeek>>>
    private val updateImageOfDayLiveData = MutableLiveData<ImageWeek>()
    val updateImageOfDay: LiveData<APIResource<Int>>

    init {
        val imageDao = ImageRoomDatabase.getDatabase(applicationContext).imageDao()
        repository = PhotoWeekRepository(imageDao)
        imagesWeek = repository.getWeeksImage()
        updateImageOfDay = updateImageOfDayLiveData.switchMap {
            repository.updateImageOfDay(it)
        }
    }

    fun updateImageInWeek(imageWeek: ImageWeek) {
        updateImageOfDayLiveData.postValue(imageWeek)
    }

    override fun onCleared() {
        super.onCleared()
        PhotoListRepository.clearRepo()
    }
}
