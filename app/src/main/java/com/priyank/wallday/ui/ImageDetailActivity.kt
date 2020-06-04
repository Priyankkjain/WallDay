package com.priyank.wallday.ui

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import coil.api.load
import com.priyank.wallday.R
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.base.Status
import com.priyank.wallday.custom.createClickableSpan
import com.priyank.wallday.custom.openApplicationSettings
import com.priyank.wallday.custom.showToast
import com.priyank.wallday.databinding.ActivityImageDetailBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.ImageDetailViewModel
import com.priyank.wallday.viewmodel.ImageViewModelFactory
import permissions.dispatcher.*
import timber.log.Timber
import java.io.File

@RuntimePermissions
class ImageDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailBinding
    private var photoItem: PhotoItem? = null
    private val photoDetailViewModel by viewModels<ImageDetailViewModel> {
        ImageViewModelFactory(getString(R.string.unsplash_key), application, true)
    }
    private val downloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private var needToGoForSettings: Boolean = false
    private var shouldDownloadEnabled: Boolean = true
    private var enqueueID: Long = -1

    private var isImageSelected: Boolean = false
    private var imageFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_detail)

        binding.toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, R.color.colorWhite))
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)

        val extras = intent.extras
        photoItem = extras?.getParcelable(Constants.EXTRA_PHOTO_ITEM)
        if (photoItem == null)
            onBackPressed()

        createClickableSpanForTheName()

        binding.fullImage.load(photoItem?.urls?.regular, builder = {
            listener({
                //THis is start for the success listener
                binding.progressCircular.visibility = View.VISIBLE
                Timber.d("Load image progress visible")
            }, {
                //THis is cancel for the success listener
                binding.progressCircular.visibility = View.GONE
            }, { r, e ->
                //This is block for the Error listener
                binding.progressCircular.visibility = View.GONE
            }, { r, source ->
                //THis is block for the success listener
                binding.progressCircular.visibility = View.GONE
            })
        })

        binding.selectPhoto.setOnClickListener {
            isImageSelected = true
            photoItem!!.links.downloadLocation?.let {
                photoDetailViewModel.callDownloadTrackerAPI(it)
            }
        }

        photoDetailViewModel.downLoadTrackerResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    Timber.d("download tracker progress visible")
                }
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    response.message?.let {
                        showToast(it)
                    }
                }
                Status.NO_NETWORK -> {
                    binding.progressCircular.visibility = View.GONE
                    showToast(R.string.no_network_available)
                }
                else -> {
                    binding.progressCircular.visibility = View.GONE
                    downLoadTheImage(response.data?.url!!)
                }
            }
        }
    }

    private fun createClickableSpanForTheName() {
        val boldFont =
            ResourcesCompat.getFont(this, R.font.nunito_bold)

        val userNameSpan = photoItem?.user?.name?.createClickableSpan({
            it.cancelPendingInputEvents()
            val browserIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getUserProfileURL())
                )
            startActivity(browserIntent)
        }, boldFont)

        val unSplashSpan = getString(R.string.unsplash).createClickableSpan({
            it.cancelPendingInputEvents()
            val browserIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.unsplash_url))
                )
            startActivity(browserIntent)
        }, boldFont)

        binding.userName.text = getString(R.string.photo_by)
        binding.userName.append(" ")
        binding.userName.append(userNameSpan)
        binding.userName.append(" " + getString(R.string.on) + " ")
        binding.userName.append(unSplashSpan)
        binding.userName.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.menu_download).isVisible = shouldDownloadEnabled
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_image_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_open_in_browser -> {
                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(photoItem!!.links.html ?: getString(R.string.unsplash_url))
                    )
                startActivity(browserIntent)
                return true
            }
            R.id.menu_download -> {
                needToGoForSettings = true
                downLoadImageWithPermissionCheck()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun downLoadTheImage(url: String) {
        val photoModel = photoItem
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription(getString(R.string.downloading_image_by, photoModel!!.user.name))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(getString(R.string.app_name))
        request.setMimeType("image/*")
        if (isImageSelected) {
            imageFileName = getFileName()
            request.setDestinationInExternalFilesDir(
                this,
                Environment.DIRECTORY_PICTURES,
                imageFileName
            )
            binding.progressCircular.visibility = View.VISIBLE
            Timber.d("Download progress bar")
        } else {
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                photoModel.user.name.plus("_" + System.currentTimeMillis()).plus(".jpg")
            )
            showToast(R.string.download_started)
        }
        enqueueID = downloadManager.enqueue(request)
    }

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (enqueueID == id) {
                if (isImageSelected) {
                    binding.progressCircular.visibility = View.GONE
                    if (imageFileName.isNullOrEmpty() || imageFileName.isNullOrBlank())
                        return

                    val downLoadedFile =
                        File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName!!)
                    if (!downLoadedFile.exists())
                        return

                    val finalIntent = Intent()
                    finalIntent.putExtra(Constants.EXTRA_IS_IMAGE_SELECTED, true)
                    finalIntent.putExtra(
                        Constants.EXTRA_SELECTED_IMAGE_PATH,
                        downLoadedFile.absolutePath
                    )
                    finalIntent.putExtra(
                        Constants.EXTRA_SELECTED_IMAGE_AUTHOR,
                        photoItem?.user?.name
                    )
                    finalIntent.putExtra(
                        Constants.EXTRA_SELECTED_IMAGE_AUTHOR_URL,
                        getUserProfileURL()
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

    private fun getFileName(): String {
        val photoModel = photoItem!!
        return photoModel.user.name.plus("_" + System.currentTimeMillis()).plus(".jpg")
    }

    private fun getUserProfileURL(): String {
        return photoItem?.user?.links?.html?.plus("?utm_source=${getString(R.string.app_name)}&utm_medium=referral")
            ?: getString(R.string.unsplash_url)
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun downLoadImage() {
        photoItem!!.links.downloadLocation?.let {
            photoDetailViewModel.callDownloadTrackerAPI(it)
        }
    }

    @OnShowRationale(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun showRationaleForImagePicker(request: PermissionRequest) {
        request.proceed()
    }

    @OnPermissionDenied(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun permissionDeniedForImagePicker() {
        showToast(R.string.storage_permission_denied)
    }

    @OnNeverAskAgain(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onNeverAskImagePickerPermission() {
        if (needToGoForSettings) {
            needToGoForSettings = false
            showToast(getString(R.string.enable_storage_permission_from_settings))
            openApplicationSettings()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.EXTRA_ACTIVITY_RESULT_REQUEST_CODE -> {
                needToGoForSettings = false
                downLoadImageWithPermissionCheck()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(onDownloadComplete)
    }
}