package com.mymodels.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

object ProfileLoader {

    fun loadAvatar(imageView: ImageView, url: String?) {

        if (url == null) return

        Glide.with(imageView.context)
            .load(url)
            .circleCrop()
            .into(imageView)

    }

}