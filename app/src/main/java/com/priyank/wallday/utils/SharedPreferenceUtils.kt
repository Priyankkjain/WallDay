package com.priyank.wallday.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64

class SharedPreferenceUtils private constructor(context: Context) {

    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor?

    init {
        val stringId = context.applicationInfo.labelRes
        pref = context.getSharedPreferences(
            context.getString(stringId) + "_SharedPreferences",
            Context.MODE_PRIVATE
        )
        editor = pref.edit()
    }

    private fun encodeValue(key: String, value: String?) {
        var encryptedValue = ""
        if (value != null) {
            encryptedValue = Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
        }
        editor?.putString(key, encryptedValue)
        editor?.commit()
    }

    private fun decodeValue(key: String, defaultValue: String): String? {
        var rslt = pref.getString(key, defaultValue)
        if (rslt != null && rslt != defaultValue) {
            rslt = String(Base64.decode(rslt, Base64.DEFAULT)) //convert bytearray to String
        }
        return rslt
    }

    fun <T> setValue(key: String, value: T) {
        encodeValue(key, value.toString())
    }

    fun <T> getValue(key: String, defaultValue: T): T {
        val decodedString = decodeValue(key, defaultValue.toString())
        return when (defaultValue) {
            is Boolean -> decodedString?.toBoolean()!! as T
            is Int -> decodedString?.toInt()!! as T
            is Long -> decodedString?.toLong()!! as T
            is Float -> decodedString?.toFloat()!! as T
            else -> decodedString.toString() as T
        }
    }

    fun clearAll() {
        editor?.clear()?.commit()
    }

    /**
     * Clear all keys from preference except given keys
     *
     * @param keysToExclude keys need to be retained
     */
    fun clearExcept(vararg keysToExclude: String) {
        pref.all.keys.forEach { key ->
            if (!keysToExclude.contains(key)) {
                editor?.remove(key)
            }
        }
        editor?.commit()
    }

    fun clearKeys(vararg keysToClear: String) {
        pref.all.keys.forEach { key ->
            if (keysToClear.contains(key)) {
                editor?.remove(key)
            }
        }
        editor?.commit()
    }

    fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    companion object {
        private var sInstance: SharedPreferenceUtils? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreferenceUtils {
            if (sInstance == null) {
                sInstance = SharedPreferenceUtils(context.applicationContext)
            }
            return sInstance as SharedPreferenceUtils
        }
    }
}


