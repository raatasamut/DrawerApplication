package com.appimake.drawerapp

import android.graphics.Typeface
import androidx.fragment.app.Fragment
import android.view.View
import com.appimake.drawerapp.expand.DAExpandableLayout

data class DAHeaderModel(
        val name: String? = null,
        val email: String? = null,
        val pictureURL: String? = null,
        val background: Any? = null,
        val onClick: DACallBack)

open class DAMenuItem(
        val tag: String = "",
        val title: Any? = null,
        val customView: Any? = null,
        val badges: DABadges? = null,
        val navBarStyle: DANavBarStyle? = null,
        val background: Any? = null,
        val moduleView: androidx.fragment.app.Fragment? = null,
        val subMenuList: ArrayList<DASubMenuItem> = ArrayList())

data class DASubMenuItem(
        val tag: String? = null,
        val title: Any? = null,
        val customView: Any? = null,
        val moduleView: androidx.fragment.app.Fragment? = null)

data class DAMenuItemDefault(
        val title: String? = null,
        val titleStyle: DATextStyle? = null,
        val icDefault: Any? = null,
        val icSelected: Any? = null,
        val onClick: DACallBack? = null)

data class DATextStyle(
        val defaultColor: String? = null,
        val selectedColor: String? = null,
        val typeFace: Typeface? = null,
        val textSize: Float = 18f)

data class DANavBarStyle(
        val background: Any? = "#FFDDAA",
        val titleColor: Any? = "#FFFFFF",
        val titleSize: Float = 18f,
        val primaryAction: DAActionItem? = null,
        val secondaryAction: DAActionItem? = null)

data class DAActionItem(
        val icon: Any? = null,
        val viewAcc: View? = null,
        val onClick: DACallBack)

data class DABadges(
        val count: String? = null,
        val color: Any? = null,
        val backgroundColor: Any? = null,
        val stroke: Int = 1,
        val shape: Int,
        val onChange: DABadgesChange)

data class DASelectedView(
        val groupView: DAExpandableLayout,
        val view: View,
        val group: DAMenuItem,
        val sub: DASubMenuItem
)