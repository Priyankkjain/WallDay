package com.priyank.wallday.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoListViewModelFactory constructor(
    private val clientID: String,
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoListViewModel(clientID, application) as T
    }

}