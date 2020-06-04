package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.api.responsemodel.DownLoadTrackerResponse
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.repository.ImageDetailRepository
import com.priyank.wallday.utils.Utils

class ImageDetailViewModel(
    private val clientID: String,
    applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val downLoadTrackerLiveData = MutableLiveData<String>()

    val downLoadTrackerResponse: LiveData<APIResource<DownLoadTrackerResponse>> =
        downLoadTrackerLiveData.switchMap {
            if (Utils.isNetworkAvailable(applicationContext)) {
                ImageDetailRepository.callDownLoadTrackerAPI(it, clientID)
            } else {
                val data = MutableLiveData<APIResource<DownLoadTrackerResponse>>()
                data.value = APIResource.noNetwork()
                data
            }
        }

    fun callDownloadTrackerAPI(url: String) {
        downLoadTrackerLiveData.postValue(url)
    }

    override fun onCleared() {
        super.onCleared()
        ImageDetailRepository.clearRepo()
    }
}
