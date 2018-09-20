package com.appimake.drawerapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.da_activity_main.*
import kotlinx.android.synthetic.main.da_activity_main.view.*
import kotlinx.android.synthetic.main.da_app_bar_main.*
import kotlinx.android.synthetic.main.da_badges.view.*
import kotlinx.android.synthetic.main.da_menu_item.view.*

abstract class DrawerAppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var defaultContentBackground = Color.BLACK
    var defaultNavStyle = DANavBarStyle(Color.BLACK, Color.WHITE, 18f, null, null)

    var navBG = Color.WHITE

    var selectedPosition = 0
    var menuItemSelectedBackgound = Color.CYAN

    abstract fun setDAHeader(): Any

    abstract fun setMenuItemList(ItemList: ArrayList<DAMenuItem>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.da_activity_main)

        if (savedInstanceState != null &&
                savedInstanceState.getInt(getString(R.string.selected_position), 0) != 0) {
            this.selectedPosition = savedInstanceState.getInt(getString(R.string.selected_position))
        }

        navigation_bar_menu.setOnClickListener {
            if (drawer_layout != null) {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setBackgroundColor(navBG)

        setHeader()
        setMenu()
    }

    private fun setHeader() {
        if (setDAHeader() is DAHeaderModel) {
            initData()
            initOnClick()
        } else {
            val viewGroup = nav_view.da_header.parent as LinearLayout
            val index = viewGroup.indexOfChild(nav_view.da_header)
            viewGroup.removeViewAt(index)

            if (setDAHeader() is View)
                viewGroup.addView(setDAHeader() as View, index)
        }
    }

    private fun initOnClick() {
        nav_view.da_profile_iv_picture.setOnClickListener {
            (setDAHeader() as DAHeaderModel).onClick.onClick(resources.getString(R.string.da_profile_click))
        }
    }

    private fun initData() {
        Glide.with(applicationContext).load((setDAHeader() as DAHeaderModel).pictureURL).into(nav_view.da_profile_iv_picture)
        nav_view.da_profile_tv_name.text = (setDAHeader() as DAHeaderModel).name
        nav_view.da_profile_tv_mail.text = (setDAHeader() as DAHeaderModel).email

        (setDAHeader() as DAHeaderModel).background.let {
            when {
                it != null -> when (it) {
                    is String -> nav_view.da_header.setBackgroundColor(Color.parseColor(it))
                    is Int -> nav_view.da_header.setBackgroundColor(it)
                }
            }
        }
    }

    private fun setMenu() {
        val menuList = ArrayList<DAMenuItem>()
        setMenuItemList(menuList)

        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)

        menuList.forEach { daMenuItem ->
            var item = LayoutInflater.from(this).inflate(R.layout.da_menu_item, layout, false)

            if (daMenuItem.customView is DAMenuItemDefault) {
                item.da_menu_item_title.text = daMenuItem.customView.title
                setMenuIC(item.da_menu_item_icon, daMenuItem.customView.icDefault)
                setFontStyle(daMenuItem.customView.titleStyle, item.da_menu_item_title, false)
            } else if (daMenuItem.customView != null) {
                item = daMenuItem.customView as View
            }

            if (daMenuItem.badges != null && item is LinearLayout)
                setBadges(daMenuItem.badges, item, layout)

            item.tag = daMenuItem

            item.setOnClickListener { p0 -> selected(nav_view.da_menu_list.indexOfChild(p0)) }
            nav_view.da_menu_list.addView(item)
        }

        selected(selectedPosition)
    }

    private fun selected(position: Int) {
        if (nav_view.da_menu_list.childCount > 0 &&
                nav_view.da_menu_list.getChildAt(position) != null &&
                (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).moduleView != null) {

            clearAction()
            unSelect()
            selectedPosition = position
            if ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault) {
                setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icSelected)
                setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, true)

                ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).let { daMenuItemDefault ->
                    if (daMenuItemDefault.onClick != null)
                        daMenuItemDefault.onClick.onClick(nav_view.da_menu_list.getChildAt(selectedPosition))
                }

            }
            nav_view.da_menu_list.getChildAt(position).setBackgroundColor(menuItemSelectedBackgound)


            setNavBar((nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).navBarStyle)

            setFMNavTitle((nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).name)

            navigation_app_bg.setBackgroundColor(defaultContentBackground)
            setContentBackground(navigation_app_bg, (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).background)

            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(R.id.da_container, (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).moduleView!!).commit()

            if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun unSelect() {
        if (nav_view.da_menu_list.childCount > 0 &&
                (nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).moduleView != null) {
            nav_view.da_menu_list.getChildAt(selectedPosition).setBackgroundColor(navBG)

            if ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault) {
                setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icDefault)
                setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, false)
            }
        }
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setMenuIC(ic: ImageView, data: Any?) {
        when {
            data != null -> when (data) {
                is String -> Glide.with(this)
                        .load(data)
                        .into(ic)
                is Drawable -> ic.setImageDrawable(data)
                is Bitmap -> ic.setImageBitmap(data)
                is Int -> ic.setImageResource(data)
            }
        }
    }

    private fun setFontStyle(style: DATextStyle?, tv: TextView, isSelected: Boolean) {
        style.let { daTextStyle ->
            when {
                daTextStyle != null -> {
                    tv.textSize = daTextStyle.textSize
                    when {
                        isSelected -> tv.setTextColor(Color.parseColor(daTextStyle.selectedColor))
                        else -> tv.setTextColor(Color.parseColor(daTextStyle.defaultColor))
                    }
                    tv.typeface = daTextStyle.typeFace
                }
            }
        }
    }

    private fun setNavBar(navStyle: DANavBarStyle?) {
        var style = defaultNavStyle

        when {
            navStyle != null -> style = navStyle
        }

        style.let {
            navigation_bar_title.textSize = it.titleSize

            when {
                it.background is String -> navigation_bar_bg.setBackgroundColor(Color.parseColor(it.background))
                it.background is Int -> navigation_bar_bg.setBackgroundColor(it.background)
            }

            when {
                it.titleColor is String -> navigation_bar_title.setTextColor(Color.parseColor(it.titleColor))
                it.titleColor is Int -> navigation_bar_title.setTextColor(it.titleColor)
            }
        }

        setAction(style.primaryAction, navigation_bar_action_primary, navigation_bar_action_primary_icon, navigation_bar_action_primary_acc)
        setAction(style.secondaryAction, navigation_bar_action_secondary, navigation_bar_action_secondary_icon, navigation_bar_action_secondary_acc)
    }

    private fun setAction(data: DAActionItem?, rl: RelativeLayout, iv: ImageView, acc: LinearLayout) {
        if (data != null) {
            rl.visibility = View.VISIBLE

            when (data.icon) {
                is String -> Glide.with(this)
                        .load(data.icon)
                        .into(iv)
                is Drawable -> iv.setImageDrawable(data.icon)
                is Bitmap -> iv.setImageBitmap(data.icon)
                is Int -> iv.setImageResource(data.icon)
            }

            if (data.viewAcc != null) {
                acc.addView(data.viewAcc)
            }

            rl.setOnClickListener {
                data.onClick.onClick(iv)
            }
        } else {
            rl.visibility = View.GONE
        }
    }

    private fun clearAction() {
        navigation_bar_action_primary.setOnClickListener {}
        navigation_bar_action_secondary.setOnClickListener {}

        navigation_bar_action_primary.visibility = View.GONE
        navigation_bar_action_secondary.visibility = View.GONE
    }

    /**
     * String : Hex
     * Int : ResID
     * Drawable : drawable
     * */
    private fun setContentBackground(v: View, data: Any?) {
        when {
            data != null -> when (data) {
                is Drawable -> v.background = data
                is String -> v.setBackgroundColor(Color.parseColor(data))
                is Int -> v.setBackgroundResource(data)
            }
        }
    }

    private fun setBadges(badges: DABadges, parent: LinearLayout, group: LinearLayout) {

        val badgesView = LayoutInflater.from(this).inflate(R.layout.da_badges, group, false)

        val gD = GradientDrawable()
        when {
            badges.backgroundColor != null -> when (badges.backgroundColor) {
                is String -> gD.setColor(Color.parseColor(badges.backgroundColor))
                is Int -> gD.setColor(badges.backgroundColor)
            }
        }
        when {
            badges.color != null -> when (badges.color) {
                is String -> {
                    gD.setStroke(badges.stroke, Color.parseColor(badges.color))
                    badgesView.da_badges_count.setTextColor(Color.parseColor(badges.color))
                }
                is Int -> {
                    gD.setStroke(badges.stroke, badges.color)
                    badgesView.da_badges_count.setTextColor(badges.color)
                }
            }
        }

        gD.shape = badges.shape

        badgesView.da_badges_bg.background = gD

        badgesView.da_badges_count.text = badges.count

        if (badges.onChange != null)
            badges.onChange.onChange(badgesView.da_badges_count)

        parent.addView(badgesView)
    }


    private fun setFMNavTitle(data: Any?) {
        when {
            data != null -> when (data) {
                is Drawable -> {
                    navigation_bar_title.visibility = View.GONE
                    navigation_bar_title_iv.visibility = View.VISIBLE
                    navigation_bar_title_iv.setImageDrawable(data)
                }
                is String -> {
                    navigation_bar_title_iv.visibility = View.GONE
                    navigation_bar_title.visibility = View.VISIBLE
                    navigation_bar_title.text = data.toString()
                }
                is Int -> {
                    navigation_bar_title.visibility = View.GONE
                    navigation_bar_title_iv.visibility = View.VISIBLE
                    navigation_bar_title_iv.setImageResource(data)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putInt(getString(R.string.selected_position), this.selectedPosition)
        super.onSaveInstanceState(outState)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        this.selectedPosition = savedInstanceState!!.getInt(getString(R.string.selected_position), 0)
    }
}