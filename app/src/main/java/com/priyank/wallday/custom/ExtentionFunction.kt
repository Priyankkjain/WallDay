package com.priyank.wallday.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
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

fun String.createClickableSpan(
    clickListener: (view: View) -> Unit,
    linkColor: Int,
    typeface: Typeface? = null
): SpannableString {
    val spannableString = SpannableString(this)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickListener.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.linkColor = linkColor
            ds.isUnderlineText = false
            ds.color = linkColor
            typeface?.let {
                ds.typeface = it
            }
        }
    }

    spannableString.setSpan(
        clickableSpan,
        0,
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

@ColorInt
fun Context.getColorResCompat(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    this.theme.resolveAttribute(id, resolvedAttr, true)
    val colorRes = resolvedAttr.run { if (resourceId != 0) resourceId else data }
    return ContextCompat.getColor(this, colorRes)
}