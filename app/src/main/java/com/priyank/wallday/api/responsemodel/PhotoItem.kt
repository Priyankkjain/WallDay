package com.priyank.wallday.api.responsemodel

import com.priyank.wallday.utils.Constants
import com.squareup.moshi.Json

data class PhotoItem(

    @Json(name = "urls")
    val urls: Urls,

    @Json(name = "color")
    val color: String? = null,

    @Json(name = "width")
    val width: Int,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "links")
    val links: Links,

    @Json(name = "id")
    val id: String,

    @Json(name = "user")
    val user: User,

    @Json(name = "height")
    val height: Int,

    @Json(name = "likes")
    val likes: Int,

    var viewType: Int = Constants.VIEW_TYPE_RECYCLE_ITEM
)

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
)

data class Links(

    @Json(name = "portfolio")
    val portfolio: String? = null,

    @Json(name = "self")
    val self: String? = null,

    @Json(name = "html")
    val html: String? = null,

    @Json(name = "photos")
    val photos: String? = null,

    @Json(name = "likes")
    val likes: String? = null,

    @Json(name = "download")
    val download: String? = null,

    @Json(name = "download_location")
    val downloadLocation: String? = null
)

data class User(

    @Json(name = "total_photos")
    val totalPhotos: Int,

    @Json(name = "twitter_username")
    val twitterUsername: String? = null,

    @Json(name = "bio")
    val bio: String? = null,

    @Json(name = "total_likes")
    val totalLikes: Int,

    @Json(name = "portfolio_url")
    val portfolioUrl: String? = null,

    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null,

    @Json(name = "name")
    val name: String,

    @Json(name = "location")
    val location: String? = null,

    @Json(name = "total_collections")
    val totalCollections: Int,

    @Json(name = "links")
    val links: Links? = null,

    @Json(name = "id")
    val id: String,

    @Json(name = "instagram_username")
    val instagramUsername: String? = null,

    @Json(name = "username")
    val username: String
)

data class ProfileImage(

    @Json(name = "small")
    val small: String? = null,

    @Json(name = "large")
    val large: String? = null,

    @Json(name = "medium")
    val medium: String? = null
)
