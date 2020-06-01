package com.priyank.wallday.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.priyank.wallday.R
import com.priyank.wallday.utils.Constants
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //Creating the notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }
    }

    @RequiresApi(26)
    private fun createNotificationChannels() {
        val name = getString(R.string.push_notification_channel_title)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(Constants.PUSH_NOTIFICATION_CHANNEL_ID, name, importance)

        mChannel.setShowBadge(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}