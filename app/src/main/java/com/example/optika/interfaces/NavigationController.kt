package com.example.optika.interfaces

import androidx.appcompat.widget.Toolbar

interface NavigationController {
    fun lockDrawer()

    fun unlockDrawer()

    fun setHomeFragmentToolbar(toolbar: Toolbar)
}