package com.priyank.wallday.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.priyank.wallday.R
import kotlin.random.Random

object NotificationUtils {
    fun showNotification(
        context: Context,
        intent: Intent,
        title: String,
        bodyOrDescription: String = ""
    ) {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(context, Constants.PUSH_NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationBuilder.setSound(defaultSoundUri)
        notificationBuilder.priority = NotificationCompat.PRIORITY_MAX
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setContentTitle(title)
        if (bodyOrDescription.isNotEmpty() && bodyOrDescription.isNotBlank())
            notificationBuilder.setContentText(bodyOrDescription)
        notificationBuilder.color = ContextCompat.getColor(context, R.color.colorAccent)
        val notificationId = Random.nextInt()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}