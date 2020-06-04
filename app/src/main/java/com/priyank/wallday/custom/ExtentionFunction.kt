package com.priyank.wallday.custom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.utils.SharedPreferenceUtils

fun Activity.showToast(@StringRes id: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, id, length).show()
}

fun Activity.showToast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, length).show()
}

fun <T> Activity.savePreferenceValue(key: String, value: T) {
    SharedPreferenceUtils.getInstance(this)
        .setValue(key, value)
}

fun <T> Activity.getPreferenceValue(key: String, defaultValue: T): T {
    return SharedPreferenceUtils.getInstance(this)
        .getValue(key, defaultValue)
}

fun Activity.openApplicationSettings(requestCode: Int = Constants.EXTRA_ACTIVITY_RESULT_REQUEST_CODE) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, requestCode)
}