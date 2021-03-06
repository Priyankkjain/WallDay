package com.priyank.wallday.utils

object Constants {
    const val PUSH_NOTIFICATION_CHANNEL_ID = "PUSH_NOTIFICATION_WALL_DAY"

    const val EXTRA_PHOTO_ITEM = "PHOTO_ITEM"
    const val EXTRA_IS_IMAGE_SELECTED = "IS_IMAGE_SELECTED"
    const val EXTRA_SELECTED_IMAGE_WEEK_POSITION = "SELECTED_IMAGE_WEEK_POSITION"
    const val EXTRA_SELECTED_IMAGE_PATH = "SELECTED_IMAGE_PATH"
    const val EXTRA_SELECTED_IMAGE_AUTHOR = "SELECTED_IMAGE_AUTHOR"
    const val EXTRA_SELECTED_IMAGE_AUTHOR_URL = "SELECTED_IMAGE_AUTHOR_URL"
    const val EXTRA_ACTIVITY_RESULT_REQUEST_CODE = 101

    const val PREF_WALL_PAPER_CHANGING_TIME = "WALL_PAPER_CHANGING_TIME"

    const val INTENT_ACTION_WALL_PAPER_CHANGE = "wallday.action.change_wallpaper"
    const val INTENT_ACTION_BOOT_COMPLETE = "android.intent.action.BOOT_COMPLETED"

    const val VIEW_TYPE_RECYCLE_ITEM = 0
    const val VIEW_TYPE_SHIMMER_ITEM = 1

    const val API_OFFSET_ITEM = 15
}