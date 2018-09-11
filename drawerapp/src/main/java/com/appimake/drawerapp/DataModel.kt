package com.appimake.drawerapp

import android.graphics.Typeface
import android.support.v4.app.Fragment

data class DAHeaderModel(
        val name: String,
        val email: String,
        val pictureURL: String,
        val background: Any?,
        val onClick: DACallBack)

data class DAMenuItem(
        val name: String,
        val customView: Any?,
        val badges: DABadges?,
        val navBarStyle: DANavBarStyle?,
        val background: Any?,
        val moduleView: Fragment?)

data class DAMenuItemDefault(
        val title: String,
        val titleStyle: DATextStyle?,
        val icDefault: Any?,
        val icSelected: Any?,
        val onClick: DACallBack?)

data class DATextStyle(
        val defaultColor: String,
        val selectedColor: String,
        val typeFace: Typeface,
        val textSize: Float)

data class DANavBarStyle(
        val background: Any?,
        val titleColor: Any?,
        val titleSize: Float,
        val primaryAction: DAActionItem?,
        val secondaryAction: DAActionItem?)

data class DAActionItem(
        val icon: Any,
        val onClick: DACallBack)

data class DABadges(
        val count: String,
        val color: Any?,
        val backgroundColor: Any?,
        val stroke: Int,
        val shape: Int,
        val onChange: DABadgesChange?)