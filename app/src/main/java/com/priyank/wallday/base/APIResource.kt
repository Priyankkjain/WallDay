package com.priyank.wallday.base

data class APIResource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, message: String? = ""): APIResource<T> {
            return APIResource(Status.SUCCESS, data, message)
        }

        fun <T> error(msg: String, data: T?): APIResource<T> {
            return APIResource(Status.ERROR, data, msg)
        }

        fun <T> noNetwork(): APIResource<T> {
            return APIResource(Status.NO_NETWORK, null, "")
        }

        fun <T> loading(data: T?): APIResource<T> {
            return APIResource(Status.LOADING, data, null)
        }
    }
}