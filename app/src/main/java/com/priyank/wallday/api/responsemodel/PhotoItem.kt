package com.priyank.wallday.api.responsemodel

import android.os.Parcelable
import com.priyank.wallday.utils.Constants
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoItem(

    @Json(name = "urls")
    val urls: Urls,

    @Json(name = "color")
    val color: String? = null,

    @Json(name = "width")
    val width: Int,

    @Json(name = "links")
    val links: Links,

    @Json(name = "id")
    val id: String,

    @Json(name = "user")
    val user: User,

    @Json(name = "height")
    val height: Int,

    var viewType: Int = Constants.VIEW_TYPE_RECYCLE_ITEM
) : Parcelable

@Parcelize
data class Urls(
    @Json(name = "small")
    val small: String? = null,

    @Json(name = "thumb")
    val thumb: String? = null,

    @Json(name = "raw")
    val raw: String? = null,

    @Json(name = "regular")
    val regular: String? = null,

    @Json(name = "full")
    val full: String? = null
) : Parcelable

@Parcelize
data class Links(
    @Json(name = "self")
    val self: String? = null,

    @Json(name = "html")
    val html: String? = null,

    @Json(name = "download")
    val download: String? = null,

    @field:Json(name = "download_location")
    val downloadLocation: String? = null
) : Parcelable

@Parcelize
data class User(
    @Json(name = "bio")
    val bio: String? = null,

    @Json(name = "name")
    val name: String,

    @Json(name = "username")
    val username: String
) : Parcelable