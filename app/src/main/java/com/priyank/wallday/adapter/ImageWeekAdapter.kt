package com.priyank.wallday.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.priyank.wallday.R
import com.priyank.wallday.database.ImageWeek
import com.priyank.wallday.databinding.ItemImageWeekBinding
import java.io.File

class ImageWeekAdapter(private val context: Context, private val data: List<ImageWeek>) :
    RecyclerView.Adapter<ImageWeekAdapter.ImageWeekVH>() {

    private var imageWeekClickListener: ImageWeekClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageWeekVH {
        val v: ItemImageWeekBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_image_week,
            parent,
            false
        )
        return ImageWeekVH(v)
    }

    override fun onBindViewHolder(holder: ImageWeekVH, position: Int) {
        val photo = data[position]

        holder.binding.weekDayTv.text =
            context.resources.getStringArray(R.array.day_of_week)[photo.dayOfWeek]

        if (photo.isImageSelected) {
            holder.binding.selectImage.visibility = View.GONE
            holder.binding.imageOfTheDay.visibility = View.VISIBLE
            holder.binding.imageOfTheDay.load(File(photo.imagePath ?: ""), builder = {
                listener({
                    //THis is start for the success listener
                    holder.binding.progressCircular.visibility = View.VISIBLE
                    holder.binding.selectImage.visibility = View.GONE
                    holder.binding.imageLoadError.visibility = View.GONE
                }, {
                    //THis is cancel for the success listener
                    holder.binding.progressCircular.visibility = View.GONE
                    holder.binding.selectImage.visibility = View.VISIBLE
                    holder.binding.imageLoadError.visibility = View.VISIBLE
                }, { r, e ->
                    //This is block for the Error listener
                    holder.binding.progressCircular.visibility = View.GONE
                    holder.binding.selectImage.visibility = View.VISIBLE
                    holder.binding.imageLoadError.visibility = View.VISIBLE
                }, { r, source ->
                    //THis is block for the success listener
                    holder.binding.progressCircular.visibility = View.GONE
                    holder.binding.selectImage.visibility = View.GONE
                    holder.binding.imageLoadError.visibility = View.GONE
                })
            })
        } else {
            holder.binding.selectImage.visibility = View.VISIBLE
            holder.binding.imageLoadError.visibility = View.GONE
            holder.binding.imageOfTheDay.visibility = View.GONE
        }

        holder.binding.selectImage.setOnClickListener {
            imageWeekClickListener?.onSelectImageClick(photo, position)
        }

        holder.binding.imageOfTheDay.setOnClickListener {
            if (photo.isImageSelected && !photo.imagePath.isNullOrEmpty() && !photo.imagePath.isNullOrBlank()) {
                Toast.makeText(context, R.string.long_press_to_remove_picture, Toast.LENGTH_LONG)
                    .show()
            }
        }

        holder.binding.imageOfTheDay.setOnLongClickListener {
            if (photo.isImageSelected && !photo.imagePath.isNullOrEmpty() && !photo.imagePath.isNullOrBlank()) {
                imageWeekClickListener?.onLongImageClick(photo, position)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ImageWeekVH(val binding: ItemImageWeekBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ImageWeekClickListener {
        fun onSelectImageClick(item: ImageWeek, position: Int)
        fun onLongImageClick(item: ImageWeek, position: Int)
    }

    fun setImageWeekClickListener(imageWeekClickListener: ImageWeekClickListener) {
        this.imageWeekClickListener = imageWeekClickListener
    }
}