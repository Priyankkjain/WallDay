package com.priyank.wallday.ui

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.priyank.wallday.BuildConfig
import com.priyank.wallday.R
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.base.Status
import com.priyank.wallday.custom.showToast
import com.priyank.wallday.databinding.ActivityImageDetailBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.PhotoDetailViewModel
import com.priyank.wallday.viewmodel.PhotoListViewModelFactory
import java.io.File


class ImageDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailBinding
    private val photoDetailViewModel by viewModels<PhotoDetailViewModel> {
        PhotoListViewModelFactory(getString(R.string.unsplash_key), application, true)
    }
    private val downloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private var shouldDownloadEnabled: Boolean = true
    private var enqueueID: Long = -1

    private var isImageSelected: Boolean = false
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_detail)

        binding.toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, R.color.colorWhite))

        val extras = intent.extras
        val photoItem = extras?.getParcelable<PhotoItem>(Constants.EXTRA_PHOTO_ITEM)
        binding.photoModel = photoItem

        binding.selectPhoto.setOnClickListener {
            isImageSelected = true
            photoDetailViewModel.callDownloadTrackerAPI(binding.photoModel.links.downloadLocation!!)
        }

        photoDetailViewModel.downLoadTrackerResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    response.message?.let {
                        showToast(it)
                    }
                }
                Status.NO_NETWORK -> {
                    showToast(R.string.no_network_available)
                }
                else -> {
                    downLoadTheImage(response.data?.url!!)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.menu_download).isEnabled = shouldDownloadEnabled
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_open_in_browser -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(binding.photoModel.urls.full))
                startActivity(browserIntent)
                return true
            }
            R.id.menu_download -> {
                photoDetailViewModel.callDownloadTrackerAPI(binding.photoModel.links.downloadLocation!!)
                return true
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun downLoadTheImage(url: String) {
        val photoModel = binding.photoModel
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription(getString(R.string.downloading_image_by, photoModel.user.name))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(getString(R.string.app_name))
        request.setMimeType("image/*")
        if (isImageSelected) {
            imageFile = createFile()
            val photoURI: Uri =
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, imageFile!!)
            request.setDestinationUri(photoURI)
        } else {
            request.setDestinationInExternalFilesDir(
                this,
                Environment.DIRECTORY_DOWNLOADS,
                photoModel.user.name.plus("_" + System.currentTimeMillis()).plus(".jpg")
            )
        }
        enqueueID = downloadManager.enqueue(request)
    }

    var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (enqueueID == id) {
                if (isImageSelected) {
                    val finalIntent = Intent()
                    finalIntent.putExtra(Constants.EXTRA_IS_IMAGE_SELECTED, true)
                    finalIntent.putExtra(
                        Constants.EXTRA_SELECTED_IMAGE_PATH,
                        imageFile?.absolutePath
                    )
                    setResult(Activity.RESULT_OK, finalIntent)
                    onBackPressed()
                } else {
                    showToast(R.string.download_successfully)
                    shouldDownloadEnabled = false
                    invalidateOptionsMenu()
                }
            }
        }
    }

    private fun createFile(): File {
        val photoModel = binding.photoModel
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File(
            storageDir,
            photoModel.user.name.plus("_" + System.currentTimeMillis()).plus(".jpg")
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(onDownloadComplete)
    }
}