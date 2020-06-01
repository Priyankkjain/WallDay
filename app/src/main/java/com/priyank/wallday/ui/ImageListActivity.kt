package com.priyank.wallday.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.priyank.wallday.R
import com.priyank.wallday.adapter.PhotoListAdapter
import com.priyank.wallday.api.requestmodel.PhotosListRequestModel
import com.priyank.wallday.api.responsemodel.Links
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.api.responsemodel.Urls
import com.priyank.wallday.api.responsemodel.User
import com.priyank.wallday.base.APIResource
import com.priyank.wallday.base.Status
import com.priyank.wallday.custom.PaginationRecyclerViewScrollListener
import com.priyank.wallday.databinding.ActivityImageListBinding
import com.priyank.wallday.utils.Constants
import com.priyank.wallday.viewmodel.PhotoListViewModel
import com.priyank.wallday.viewmodel.PhotoListViewModelFactory

class ImageListActivity : AppCompatActivity(), PhotoListAdapter.PhotoImageClickListener {

    private lateinit var binding: ActivityImageListBinding

    private val bikeListViewModel by viewModels<PhotoListViewModel> {
        PhotoListViewModelFactory(getString(R.string.unsplash_key), application)
    }

    private lateinit var bikeListAdapter: PhotoListAdapter
    private lateinit var bikeList: MutableList<PhotoItem>

    private var pageNo = 1
    private lateinit var endlessRecyclerViewScrollListener: PaginationRecyclerViewScrollListener
    private var isLoadingData = false
    private var isLastPage = false

    private lateinit var bikeListRequestModel: PhotosListRequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_list)

        bikeListRequestModel = PhotosListRequestModel(pageNo)

        bikeList = mutableListOf()

        bikeListAdapter = PhotoListAdapter(bikeList)
        bikeListAdapter.setPhotoImageClickListener(this)
        binding.photosRv.adapter = bikeListAdapter

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.photosRv.layoutManager = staggeredGridLayoutManager

        bikeListViewModel.photoListResponse.observe(this, ::handleBikeListResponse)

        endlessRecyclerViewScrollListener =
            object : PaginationRecyclerViewScrollListener(binding.photosRv.layoutManager!!) {
                override fun onLoadMore(totalItemsCount: Int, view: RecyclerView?) {
                    if (!isLoadingData && !isLastPage) {
                        pageNo++
                        bikeListRequestModel.page = pageNo
                        isLoadingData = true
                        callBikeListAPI()
                    }
                }
            }
        binding.photosRv.addOnScrollListener(endlessRecyclerViewScrollListener)

        binding.swipeRefresh.setOnRefreshListener {
            pageNo = 1
            isLastPage = false
            bikeListRequestModel.page = pageNo
            bikeList.clear()
            bikeListAdapter.notifyDataSetChanged()
            callBikeListAPI()
        }

        if (!isLastPage)
            callBikeListAPI()
    }

    private fun callBikeListAPI() {
        bikeListViewModel.callPhotoListAPI(bikeListRequestModel)
    }

    private fun handleBikeListResponse(response: APIResource<PhotoItem>) {

        if (binding.swipeRefresh.isRefreshing)
            binding.swipeRefresh.isRefreshing = false

        when (response.status) {
            Status.LOADING -> {
                bikeList.addAll(getShimmerDataList())
                bikeListAdapter.notifyItemRangeInserted(
                    bikeList.size - Constants.API_OFFSET_ITEM,
                    Constants.API_OFFSET_ITEM
                )
            }
            Status.ERROR -> {
                if (pageNo == 1) {
                    bikeList.clear()
                    bikeListAdapter.notifyDataSetChanged()
                    binding.noDataFoundTV.visibility = View.VISIBLE
                    binding.bikesRV.visibility = View.GONE
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

                val data = response.data!!
                val apiList = data.data

                if (apiList.isEmpty() && pageNo == 1) {
                    binding.noDataFoundTV.visibility = View.VISIBLE
                    binding.bikesRV.visibility = View.GONE
                    return
                }

                val apiListSize = apiList.size
                bikeList.addAll(apiList)
                bikeListAdapter.notifyItemRangeInserted(bikeList.size - apiListSize, apiListSize)

                isLastPage = bikeList.size == data.count

                isLoadingData = false
            }
        }
    }

    private fun removeShimmerItemsFromList() {
        val reverseBikeList = bikeList.asReversed()
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
            bikeList.removeAll(shimmerList)
            bikeListAdapter.notifyItemRangeRemoved(bikeList.size, totalShimmerSize)
        }
    }

    private fun getShimmerDataList(): List<PhotoItem> {
        //This is fake list it will show as shimmer view so that the data will not be shown.
        val bikeShimmerList = mutableListOf<PhotoItem>()
        repeat(Constants.API_OFFSET_ITEM) {
            bikeShimmerList.add(
                PhotoItem(
                    Urls(),"",0,"","", Links(),"", User(0,)
                )
            )
        }
        return bikeShimmerList
    }


    override fun onBikeImageClick(view: View, item: PhotoItem, position: Int) {

    }
}