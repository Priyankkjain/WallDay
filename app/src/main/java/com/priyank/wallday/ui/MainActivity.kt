package com.priyank.wallday.ui

import android.app.Activity
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
import com.priyank.wallday.custom.showToast
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.databinding.ActivityMainBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.ImageWeekViewModel

class MainActivity : AppCompatActivity(), ImageWeekAdapter.ImageWeekClickListener {

    private lateinit var binding: ActivityMainBinding
    private val imageWeekViewModel by viewModels<ImageWeekViewModel>()

    private lateinit var imageWeekAdapter: ImageWeekAdapter

    private var imageWeekList = mutableListOf<ImageWeek>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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