package com.mymodels.utils

import android.content.Context
import android.widget.Toast

object NotificationHelper {

    fun show(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}