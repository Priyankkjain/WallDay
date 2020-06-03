package com.priyank.wallday.api.responsemodel

import com.squareup.moshi.Json

data class DownLoadTrackerResponse(

	@Json(name="url")
	val url: String
)
