package com.priyank.wallday.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.priyank.wallday.R
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.databinding.ItemImageListBinding
import com.priyank.wallday.databinding.ItemImageListShimmerBinding
import com.priyank.wallday.utils.Constants

class ImageListAdapter(private val data: List<PhotoItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photoImageClickListener: PhotoImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constants.VIEW_TYPE_RECYCLE_ITEM) {
            val v: ItemImageListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_image_list,
                parent,
                false
            )
            return PhotoVH(v)
        } else {
            val v: ItemImageListShimmerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_image_list_shimmer,
                parent,
                false
            )
            return PhotoShimmerVH(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoVH) {
            val photo = data[position]
            holder.binding.photoModel = photo

            holder.binding.itemUnsplashPhotoImageView.aspectRatio =
                photo.height.toDouble() / photo.width.toDouble()

            if (!photo.color.isNullOrEmpty())
                holder.itemView.setBackgroundColor(Color.parseColor(photo.color))

            holder.binding.root.setOnClickListener {
                photoImageClickListener?.onImageClick(it, photo, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PhotoVH(val binding: ItemImageListBinding) : RecyclerView.ViewHolder(binding.root)

    inner class PhotoShimmerVH(val binding: ItemImageListShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PhotoImageClickListener {
        fun onImageClick(view: View, item: PhotoItem, position: Int)
    }

    fun setPhotoImageClickListener(photoImageClickListener: PhotoImageClickListener) {
        this.photoImageClickListener = photoImageClickListener
    }
}