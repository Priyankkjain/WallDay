package com.priyank.wallday.schedular

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.utils.SharedPreferenceUtils
import timber.log.Timber
import java.util.*


class BootCompleteBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.d("Inside Boot Complete Extend broadcaster")

        if (intent?.action != Constants.INTENT_ACTION_BOOT_COMPLETE)
            return

        val calendar = Calendar.getInstance()
        val wallPaperChangingTime =
            SharedPreferenceUtils.getInstance(context!!)
                .getValue(Constants.PREF_WALL_PAPER_CHANGING_TIME, "12:00 AM")
        val hour = wallPaperChangingTime.substring(0..1)
        val minute = wallPaperChangingTime.substring(3..4)
        val amPM = wallPaperChangingTime.takeLast(2)
        calendar[Calendar.HOUR] = hour.toInt()
        calendar[Calendar.MINUTE] = minute.toInt()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.AM_PM] = if (amPM == "AM") Calendar.AM else Calendar.PM

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = Intent(context, ExtendBikeBroadcastReceiver::class.java)
        alarmIntent.action = Constants.INTENT_ACTION_WALL_PAPER_CHANGE
        val pendingIntent =
            PendingIntent.getBroadcast(context, 101, alarmIntent, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}