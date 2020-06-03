package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.api.responsemodel.DownLoadTrackerResponse
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.repository.PhotoDetailRepository
import com.priyank.wallday.repository.PhotoListRepository

class PhotoDetailViewModel(
    private val clientID: String,
    private val applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val downLoadTrackerLiveData = MutableLiveData<String>()

    val downLoadTrackerResponse: LiveData<APIResource<DownLoadTrackerResponse>> =
        downLoadTrackerLiveData.switchMap {
            PhotoDetailRepository.callDownLoadTrackerAPI(clientID, it)
        }

    fun callDownloadTrackerAPI(url: String) {
        downLoadTrackerLiveData.postValue(url)
    }

    override fun onCleared() {
        super.onCleared()
        PhotoListRepository.clearRepo()
    }
}
