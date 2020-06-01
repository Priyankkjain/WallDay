package com.priyank.wallday.api.requestmodel

import com.priyank.wallday.utils.Constants
import com.squareup.moshi.Json

data class PhotosListRequestModel(
    @Json(name = "page")
    var page: Int,

    @Json(name = "per_page")
    val perPage: Int = Constants.API_OFFSET_ITEM
)
