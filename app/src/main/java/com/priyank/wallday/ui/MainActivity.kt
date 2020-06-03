package com.priyank.wallday.ui

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.priyank.wallday.R
import com.priyank.wallday.adapter.ImageWeekAdapter
import com.priyank.wallday.base.Status
import com.priyank.wallday.custom.getPreferenceValue
import com.priyank.wallday.custom.savePreferenceValue
import com.priyank.wallday.custom.showToast
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.databinding.ActivityMainBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.ImageWeekViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), ImageWeekAdapter.ImageWeekClickListener {

    private lateinit var binding: ActivityMainBinding
    private val imageWeekViewModel by viewModels<ImageWeekViewModel>()

    private lateinit var imageWeekAdapter: ImageWeekAdapter

    private var imageWeekList = mutableListOf<ImageWeek>()
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val wallPaperChangingTime =
            getPreferenceValue(Constants.PREF_WALL_PAPER_CHANGING_TIME, "15:00")
        binding.wallPaperChangingTime.text =
            getString(R.string.wall_paper_changing_time, wallPaperChangingTime)
        SimpleDateFormat("HH:mm", Locale.US).parse(wallPaperChangingTime)?.let {
            calendar.time = it
        }
        binding.wallPaperChangingTime.setOnClickListener {
            showTimePicker()
        }
        changeTheAlarmManager()

        imageWeekAdapter = ImageWeekAdapter(this, imageWeekList)
        imageWeekAdapter.setImageWeekClickListener(this)
        binding.imageWeekViewPager.adapter = imageWeekAdapter

        with(binding.imageWeekViewPager) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
        binding.imageWeekViewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * offsetPx + pageMarginPx)
            page.translationX = offset
        }

        imageWeekViewModel.imagesWeek.observe(this) { response ->
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
                else -> {
                    imageWeekList.clear()
                    imageWeekList.addAll(response.data!!)
                    imageWeekAdapter.notifyDataSetChanged()
                }
            }
        }

        imageWeekViewModel.updateImageOfDay.observe(this) { response ->
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
                else -> {

                }
            }
        }
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val storeHour = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
            val storeMinute = if (minute < 10) "0$minute" else "$minute"
            savePreferenceValue(Constants.PREF_WALL_PAPER_CHANGING_TIME, "$storeHour:$storeMinute")
            binding.wallPaperChangingTime.text =
                getString(R.string.wall_paper_changing_time, "$storeHour:$storeMinute")
            changeTheAlarmManager()
        }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], false)
        timePickerDialog.show()
    }

    private fun changeTheAlarmManager() {
        /*val wallpaperManager =
            WallpaperManager.getInstance(applicationContext)
        val bitmap = BitmapFactory.decodeFile("")
        wallpaperManager.setBitmap(bitmap)*/
    }

    override fun onSelectImageClick(item: ImageWeek, position: Int) {
        val intent = Intent(this, ImageListActivity::class.java)
        intent.putExtra(Constants.EXTRA_SELECT_IMAGE_WEEK_POSITION, position)
        imageSelectResult.launch(intent)
    }

    private val imageSelectResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val isImageSelected =
                        data.getBooleanExtra(Constants.EXTRA_IS_IMAGE_SELECTED, false)
                    if (isImageSelected) {
                        val position =
                            data.getIntExtra(Constants.EXTRA_SELECT_IMAGE_WEEK_POSITION, 0)
                        val path =
                            data.getStringExtra(Constants.EXTRA_SELECTED_IMAGE_PATH)

                        path?.let {
                            imageWeekList[position].imagePath = it
                            imageWeekList[position].isImageSelected = true
                            imageWeekAdapter.notifyItemChanged(position)
                            imageWeekViewModel.updateImageInWeek(imageWeekList[position])
                        }
                    }
                }
            }
        }
}