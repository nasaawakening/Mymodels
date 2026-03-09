package com.mymodels.ui.sidebar

import android.app.Activity
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import com.mymodels.R

class SidebarMenu(
    private val activity: Activity,
    private val drawer: DrawerLayout
){

    fun open(){
        drawer.openDrawer(Gravity.LEFT)
    }

    fun close(){
        drawer.closeDrawer(Gravity.LEFT)
    }
}
