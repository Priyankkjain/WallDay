package com.priyank.wallday.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.priyank.wallday.R
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.utils.SharedPreferenceUtils

fun Activity.showToast(@StringRes id: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, id, length).show()
}

fun Activity.showToast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, length).show()
}

fun Fragment.showToast(@StringRes id: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), id, length).show()
}

fun Fragment.showToast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), text, length).show()
}

fun <T> Activity.gotoActivity(
    activity: Class<T>,
    bundle: Bundle? = null,
    needToFinish: Boolean = true,
    clearAllActivity: Boolean = false
) {
    val intent = Intent(this, activity)

    if (bundle != null)
        intent.putExtras(bundle)

    if (clearAllActivity)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

    startActivity(intent)

    if (needToFinish)
        finish()

    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun <T> Activity.savePreferenceValue(key: String, value: T) {
    SharedPreferenceUtils.getInstance(this)
        .setValue(key, value)
}

fun <T> Fragment.savePreferenceValue(key: String, value: T) {
    SharedPreferenceUtils.getInstance(requireContext())
        .setValue(key, value)
}

fun <T> Activity.getPreferenceValue(key: String, defaultValue: T): T {
    return SharedPreferenceUtils.getInstance(this)
        .getValue(key, defaultValue)
}

fun <T> Fragment.getPreferenceValue(key: String, defaultValue: T): T {
    return SharedPreferenceUtils.getInstance(requireContext())
        .getValue(key, defaultValue)
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.createClickableSpan(
    clickListener: (view: View) -> Unit,
    typeface: Typeface? = null
): SpannableString {
    val spannableString = SpannableString(this)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickListener.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
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

fun Fragment.hideKeyboard(view: View) {
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Fragment.openApplicationSettings(requestCode: Int = Constants.EXTRA_ACTIVITY_RESULT_REQUEST_CODE) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", requireActivity().packageName, null)
    intent.data = uri
    startActivityForResult(intent, requestCode)
}

fun Float.twoDecimal() = String.format("%.02f", this)