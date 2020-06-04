package com.priyank.wallday.utils

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.NonNull
import com.priyank.wallday.database.ImageWeek
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.InputStream


class Utils {

    companion object {
        fun isAppOnForeground(context: Context): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appProcesses = activityManager.runningAppProcesses ?: return false
            for (appProcess in appProcesses) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == context.packageName) {
                    return true
                }
            }
            return false
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return (netInfo != null && netInfo.isConnectedOrConnecting
                    && cm.activeNetworkInfo.isAvailable
                    && cm.activeNetworkInfo.isConnected)
        }

        fun getWeekModelsForFirstTime(): List<ImageWeek> {
            val imageWeekList = mutableListOf<ImageWeek>()
            repeat(7) {
                imageWeekList.add(ImageWeek(it, false, ""))
            }
            return imageWeekList
        }

        fun getRequestBody(requestString: String): RequestBody {
            return requestString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }

        fun getFilePath(context: Context, uriFile: Uri): String? {
            var uri = uriFile
            var selection: String? = null
            var selectionArgs: Array<String>? = null
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                isDownloadsDocument(uri) -> {
                    val id = DocumentsContract.getDocumentId(uri)
                    uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("image" == type) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
            if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                if (isGooglePhotosUri(uri)) {
                    return uri.lastPathSegment
                }

                val projection = arrayOf(MediaStore.Images.Media.DATA)
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver
                        .query(uri, projection, selection, selectionArgs, null)
                    val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (cursor.moveToFirst()) {
                        if (cursor.getString(column_index) != null)
                            return cursor.getString(column_index)
                        else return ""
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }

        fun isChromeCustomTabsSupported(@NonNull context: Context): Boolean {
            val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService")
            serviceIntent.setPackage("com.android.chrome")
            val resolveInfos = context.packageManager.queryIntentServices(serviceIntent, 0)
            return resolveInfos.isNotEmpty()
        }

        fun loadJSONFromAsset(isCommonCountry: Boolean, context: Context): String? {
            val json: String?
            val inputStream: InputStream
            try {
                inputStream = if (isCommonCountry) {
                    context.assets.open("common_countries.json")
                } else {
                    context.assets.open("european_countries.json")
                }
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                return null
            }
            return json
        }
    }

}