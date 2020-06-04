package com.priyank.wallday.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.priyank.wallday.R
import com.priyank.wallday.adapter.ImageListAdapter
import com.priyank.wallday.api.requestmodel.ImageListRequestModel
import com.priyank.wallday.api.responsemodel.Links
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.api.responsemodel.Urls
import com.priyank.wallday.api.responsemodel.User
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.base.Status
import com.priyank.wallday.custom.PaginationRecyclerViewScrollListener
import com.priyank.wallday.custom.showToast
import com.priyank.wallday.databinding.ActivityImageListBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.ImageListViewModel
import com.priyank.wallday.viewmodel.ImageViewModelFactory


class ImageListActivity : AppCompatActivity(), ImageListAdapter.ImageClickListener {

    private lateinit var binding: ActivityImageListBinding
    private var selectedImagePosition = 0

    private val photoListViewModel by viewModels<ImageListViewModel> {
        ImageViewModelFactory(getString(R.string.unsplash_key), application)
    }

    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var photoList: MutableList<PhotoItem>

    private var pageNo = 1
    private var maxPage = 50
    private lateinit var endlessRecyclerViewScrollListener: PaginationRecyclerViewScrollListener
    private var isLoadingData = false
    private var isLastPage = false

    private lateinit var imageListRequestModel: ImageListRequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_list)
        setSupportActionBar(binding.topAppBar)

        selectedImagePosition =
            intent.getIntExtra(Constants.EXTRA_SELECTED_IMAGE_WEEK_POSITION, 0)

        imageListRequestModel = ImageListRequestModel(pageNo)

        photoList = mutableListOf()

        imageListAdapter = ImageListAdapter(photoList)
        imageListAdapter.setPhotoImageClickListener(this)
        binding.photosRv.adapter = imageListAdapter

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.photosRv.layoutManager = staggeredGridLayoutManager

        photoListViewModel.photoListResponse.observe(this) {
            handlePhotoListResponse(it)
        }

        endlessRecyclerViewScrollListener =
            object : PaginationRecyclerViewScrollListener(binding.photosRv.layoutManager!!) {
                override fun onLoadMore(totalItemsCount: Int, view: RecyclerView?) {
                    if (!isLoadingData && !isLastPage) {
                        pageNo++
                        imageListRequestModel.page = pageNo
                        isLoadingData = true
                        if (pageNo != maxPage)
                            callPhotoListAPI()
                        else {
                            isLastPage = true
                        }
                    }
                }
            }
        binding.photosRv.addOnScrollListener(endlessRecyclerViewScrollListener)

        binding.swipeRefresh.setOnRefreshListener {
            pageNo = 1
            isLastPage = false
            imageListRequestModel.page = pageNo
            photoList.clear()
            imageListAdapter.notifyDataSetChanged()
            callPhotoListAPI()
        }

        if (!isLastPage)
            callPhotoListAPI()
    }

    private fun callPhotoListAPI() {
        photoListViewModel.callPhotoListAPI(imageListRequestModel)
    }

    private fun handlePhotoListResponse(response: APIResource<List<PhotoItem>>) {

        if (binding.swipeRefresh.isRefreshing)
            binding.swipeRefresh.isRefreshing = false

        when (response.status) {
            Status.LOADING -> {
                photoList.addAll(getShimmerDataList())
                imageListAdapter.notifyItemRangeInserted(
                    photoList.size - Constants.API_OFFSET_ITEM,
                    Constants.API_OFFSET_ITEM
                )
            }
            Status.ERROR -> {
                if (pageNo == 1) {
                    photoList.clear()
                    imageListAdapter.notifyDataSetChanged()
                    binding.photosRv.visibility = View.GONE
                } else {
                    removeShimmerItemsFromList()
                }
                isLastPage = true
                response.message?.let {
                    showToast(it)
                }
            }
            Status.NO_NETWORK -> {
                showToast(R.string.no_network_available)
            }
            else -> {
                removeShimmerItemsFromList()

                val apiList = response.data!!

                if (apiList.isEmpty() && pageNo == 1) {
                    binding.photosRv.visibility = View.GONE
                    return
                }

                val apiListSize = apiList.size
                photoList.addAll(apiList)
                imageListAdapter.notifyItemRangeInserted(photoList.size - apiListSize, apiListSize)
                isLoadingData = false
            }
        }
    }

    private fun removeShimmerItemsFromList() {
        val reverseBikeList = photoList.asReversed()
        val shimmerList = mutableListOf<PhotoItem>()
        for (dataItem in reverseBikeList) {
            if (dataItem.viewType == Constants.VIEW_TYPE_SHIMMER_ITEM) {
                shimmerList.add(dataItem)
            } else {
                break
            }
        }

        if (shimmerList.size != 0) {
            val totalShimmerSize = shimmerList.size
            photoList.removeAll(shimmerList)
            imageListAdapter.notifyItemRangeRemoved(photoList.size, totalShimmerSize)
        }
    }

    private fun getShimmerDataList(): List<PhotoItem> {
        //This is fake list it will show as shimmer view so that the data will not be shown.
        val bikeShimmerList = mutableListOf<PhotoItem>()
        repeat(Constants.API_OFFSET_ITEM) {
            bikeShimmerList.add(
                PhotoItem(
                    Urls(),
                    "",
                    0,
                    Links(),
                    "",
                    User("", "", ""),
                    0,
                    viewType = Constants.VIEW_TYPE_SHIMMER_ITEM
                )
            )
        }
        return bikeShimmerList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageClick(view: View, item: PhotoItem, position: Int) {
        val activityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "imageTransition")
        val intent = Intent(this, ImageDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_SELECTED_IMAGE_WEEK_POSITION, position)
        intent.putExtra(Constants.EXTRA_PHOTO_ITEM, item)
        imageSelectResult.launch(intent, activityOptionsCompat)
    }

    private val imageSelectResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val isImageSelected =
                        data.getBooleanExtra(Constants.EXTRA_IS_IMAGE_SELECTED, false)
                    if (isImageSelected) {
                        val path =
                            data.getStringExtra(Constants.EXTRA_SELECTED_IMAGE_PATH)
                        path?.let {
                            val intent = Intent()
                            intent.putExtra(
                                Constants.EXTRA_SELECTED_IMAGE_WEEK_POSITION,
                                selectedImagePosition
                            )
                            intent.putExtra(
                                Constants.EXTRA_SELECTED_IMAGE_PATH,
                                it
                            )
                            intent.putExtra(
                                Constants.EXTRA_IS_IMAGE_SELECTED,
                                true
                            )
                            intent.putExtra(
                                Constants.EXTRA_SELECTED_IMAGE_AUTHOR,
                                data.getStringExtra(Constants.EXTRA_SELECTED_IMAGE_AUTHOR)
                            )
                            intent.putExtra(
                                Constants.EXTRA_SELECTED_IMAGE_AUTHOR_URL,
                                data.getStringExtra(Constants.EXTRA_SELECTED_IMAGE_AUTHOR_URL)
                            )
                            setResult(Activity.RESULT_OK, intent)
                            onBackPressed()
                        }
                    }
                }
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}