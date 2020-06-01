package com.priyank.wallday.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
object DateTimeUtils {


    const val DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_FULL_TIME_AM_PM = "dd MMM yyyy, HH:mm aa"
    const val FORMAT_DATE = "dd/MM/yyyy"
    const val FORMAT_DATE_DD_MMM_YYYY = "dd MMM yyyy"
    const val FORMAT_TIME_AM_PM = "HH:mm a"
    const val FORMAT_TIME_FULL = "HH:mm:ss"

    fun relativeToCurrentTime(dateTime: Long): String {

        val simpleDateFormat = SimpleDateFormat("MMM dd")
        val simpleTimeFormat = SimpleDateFormat("HH:mm a")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateTime

        if (dateTime == 0L) {
            return ""
        }

        var diff = System.currentTimeMillis() - dateTime

        if (diff < 0)
            diff = diff.unaryMinus()

        val hours = TimeUnit.MILLISECONDS.toHours(diff)

        if (hours > 48) {
            return simpleDateFormat.format(calendar.time)
        } else if (hours > 24) {
            return "Yesterday"
        } else {
            return simpleTimeFormat.format(calendar.time)
        }
    }

    fun getDateInMillieSeconds(dateTime: String, inputFormat: String): Long {
        val simpleDateFormat = SimpleDateFormat(inputFormat)
        return simpleDateFormat.parse(dateTime)?.time ?: 0
    }

    fun getDateStringAccordingToFormat(date: Date, outputFormat: String): String {
        val simpleDateFormat = SimpleDateFormat(outputFormat)
        return simpleDateFormat.format(date) ?: ""
    }

    fun getDateStringAccordingToFormat(
        date: String,
        inputFormat: String = DATE_FORMAT_FULL,
        outputFormat: String = FORMAT_DATE
    ): String {

        if (date.isBlank() or date.isEmpty())
            return ""

        val simpleDateFormat = SimpleDateFormat(outputFormat)
        val inputDate = Date(getDateInMillieSeconds(date, inputFormat))
        return simpleDateFormat.format(inputDate) ?: ""
    }
}