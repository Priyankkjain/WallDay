package com.priyank.wallday.receiver

import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import com.priyank.wallday.R
import com.priyank.wallday.database.ImageRoomDatabase
import com.priyank.wallday.ui.MainActivity
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.utils.NotificationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*


class WallPaperChangeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.d("Inside Wall Paper broadcaster")

        if (intent?.action != Constants.INTENT_ACTION_WALL_PAPER_CHANGE)
            return

        val calendar = Calendar.getInstance()
        val dayOfTheWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        val imageDao = ImageRoomDatabase.getDatabase(context!!).imageDao()
        val disposable = imageDao.getDayImage(dayOfTheWeek)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseModel ->
                if (responseModel != null) {
                    val wallpaperManager =
                        WallpaperManager.getInstance(context)
                    val bitmap = BitmapFactory.decodeFile(responseModel.imagePath)
                    if (bitmap != null) {
                        wallpaperManager.setBitmap(bitmap)
                        val selectedIntent: Intent
                        val selectedMessage: String
                        if (responseModel.authorURL.isNullOrEmpty() || responseModel.authorURL.isNullOrBlank()) {
                            selectedIntent = Intent(context, MainActivity::class.java)
                            selectedIntent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            selectedMessage = context.getString(
                                R.string.wall_paper_changed_successfully_without_author_profile,
                                responseModel.authorName
                            )
                        } else {
                            selectedIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(responseModel.authorURL))
                            selectedMessage = context.getString(
                                R.string.wall_paper_changed_successfully,
                                responseModel.authorName
                            )
                        }
                        NotificationUtils.showNotification(context, selectedIntent, selectedMessage)
                    }
                }
            }, { e ->
                Timber.e(e)
            })
    }
}