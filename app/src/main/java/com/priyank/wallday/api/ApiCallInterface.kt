package com.priyank.wallday.api

import com.priyank.wallday.api.requestmodel.PhotosListRequestModel
import com.priyank.wallday.api.responsemodel.DownLoadTrackerResponse
import com.priyank.wallday.api.responsemodel.PhotoItem
import io.reactivex.Single
import retrofit2.http.*

interface ApiCallInterface {

    @POST(APIConstants.API_LIST_OF_PHOTOS)
    fun callPhotosAPI(
        @Query("client_id") clientID: String,
        @Body requestBody: PhotosListRequestModel
    ): Single<List<PhotoItem>>

    @GET
    fun callDownLoadTrackerAPI(
        @Url url: String,
        @Query("client_id") clientID: String
    ): Single<DownLoadTrackerResponse>
}