package com.mymodels.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mymodels.R

object ProfileLoader {

    fun loadAvatar(imageView: ImageView, url: String?) {

        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_user) 
            .error(R.drawable.ic_user)
            .circleCrop()
            .into(imageView)

    }

}