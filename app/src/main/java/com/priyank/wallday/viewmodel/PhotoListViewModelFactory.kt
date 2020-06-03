package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoListViewModelFactory constructor(
    private val clientID: String,
    private val application: Application,
    private val isPhotoDetail: Boolean = false
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (isPhotoDetail)
            PhotoDetailViewModel(clientID, application) as T
        else
            PhotoListViewModel(clientID, application) as T
    }

}