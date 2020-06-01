package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.api.requestmodel.PhotosListRequestModel
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.repository.PhotoListRepository
import com.priyank.wallday.utils.Utils

class PhotoListViewModel(
    private val clientID: String,
    private val applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val photoListRequestModelLiveData = MutableLiveData<PhotosListRequestModel>()

    val photoListResponse: LiveData<APIResource<List<PhotoItem>>> =
        if (Utils.isNetworkAvailable(applicationContext)) {
            photoListRequestModelLiveData.switchMap {
                PhotoListRepository.callPhotoListAPI(clientID, it)
            }
        } else {
            val data = MutableLiveData<APIResource<List<PhotoItem>>>()
            data.value = APIResource.noNetwork()
            data
        }

    fun callPhotoListAPI(photoListRequestModel: PhotosListRequestModel) {
        photoListRequestModelLiveData.postValue(photoListRequestModel)
    }

    override fun onCleared() {
        super.onCleared()
        PhotoListRepository.clearRepo()
    }
}
