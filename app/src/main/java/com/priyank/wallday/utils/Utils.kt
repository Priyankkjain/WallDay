package com.priyank.wallday.utils

import android.content.Context
import android.net.ConnectivityManager
import com.priyank.wallday.database.ImageWeek


class Utils {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return (netInfo != null && netInfo.isConnectedOrConnecting
                    && cm.activeNetworkInfo.isAvailable
                    && cm.activeNetworkInfo.isConnected)
        }

        fun getWeekModelsForFirstTime(): List<ImageWeek> {
            val imageWeekList = mutableListOf<ImageWeek>()
            repeat(7) {
                imageWeekList.add(ImageWeek(it, false, "", "", ""))
            }
            return imageWeekList
        }
    }
}