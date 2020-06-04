package com.priyank.wallday.api

import com.priyank.wallday.api.responsemodel.DownLoadTrackerResponse
import com.priyank.wallday.api.responsemodel.PhotoItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiCallInterface {

    @GET("photos")
    fun callPhotosAPI(
        @Query("client_id") clientID: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<PhotoItem>>

    @GET
    fun callDownLoadTrackerAPI(
        @Url url: String,
        @Query("client_id") clientID: String
    ): Single<DownLoadTrackerResponse>
}