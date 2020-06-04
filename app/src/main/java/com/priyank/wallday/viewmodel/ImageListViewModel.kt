package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.priyank.wallday.api.requestmodel.ImageListRequestModel
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.repository.ImageListRepository
import com.priyank.wallday.utils.Utils

class ImageListViewModel(
    private val clientID: String,
    applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val photoListRequestModelLiveData = MutableLiveData<ImageListRequestModel>()

    val photoListResponse: LiveData<APIResource<List<PhotoItem>>> =
        photoListRequestModelLiveData.switchMap {
            if (Utils.isNetworkAvailable(applicationContext)) {
                ImageListRepository.callPhotoListAPI(clientID, it)
            } else {
                val data = MutableLiveData<APIResource<List<PhotoItem>>>()
                data.value = APIResource.noNetwork()
                data
            }
        }

    fun callPhotoListAPI(photoListRequestModel: ImageListRequestModel) {
        photoListRequestModelLiveData.postValue(photoListRequestModel)
    }

    override fun onCleared() {
        super.onCleared()
        ImageListRepository.clearRepo()
    }
}
