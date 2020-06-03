package com.priyank.wallday.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.priyank.wallday.R
import com.priyank.wallday.api.responsemodel.PhotoItem
import com.priyank.wallday.databinding.ItemPhotoBinding

class PhotoListAdapter(private val data: List<PhotoItem>) :
    RecyclerView.Adapter<PhotoListAdapter.PhotoVH>() {

    private var photoImageClickListener: PhotoImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val v: ItemPhotoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_photo,
            parent,
            false
        )
        return PhotoVH(v)
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
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

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PhotoVH(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    interface PhotoImageClickListener {
        fun onImageClick(view: View, item: PhotoItem, position: Int)
    }

    fun setPhotoImageClickListener(photoImageClickListener: PhotoImageClickListener) {
        this.photoImageClickListener = photoImageClickListener
    }
}