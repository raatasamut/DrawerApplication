package com.appimake.drawerapp

import android.graphics.Typeface
import android.support.v4.app.Fragment

data class DAUserDataModel(val name: String, val email: String, val pictureURL: String, val onClick: DACallBack)

data class DAMenuItem(val name: String, val customView: Any?, val navBarStyle: DANavBarStyle?, val background: Any?, val moduleView: Fragment?)

data class DAMenuItemDefault(val title: String, val titleStyle: DATextStyle?, val icDefault: Any?, val icSelected: Any?, val onClick: DACallBack?)

data class DATextStyle(val defaultColor: String, val selectedColor: String, val typeFace: Typeface, val textSize: Float)

data class DANavBarStyle(val background: Any?, val titleColor: Any?, val titleSize: Float)