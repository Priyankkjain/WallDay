package com.priyank.wallday.api

import com.priyank.wallday.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class ApiHelperClass private constructor() {

    private var apiService: ApiCallInterface

    init {
        val timeOut: Long = 30 * 1000

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
            .readTimeout(timeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(timeOut, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiCallInterface::class.java)
    }

    companion object {
        private var apiClient: ApiCallInterface? = null
        fun getAPIClient(): ApiCallInterface {
            if (apiClient == null) {
                apiClient = ApiHelperClass().apiService
            }
            return apiClient!!
        }
    }
}