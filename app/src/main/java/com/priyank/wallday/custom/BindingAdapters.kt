package com.priyank.wallday.custom

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.priyank.wallday.R


@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, url: String?) {
    imageView.load(url, builder = {
        this.error(R.drawable.ic_launcher_foreground)
    })
}