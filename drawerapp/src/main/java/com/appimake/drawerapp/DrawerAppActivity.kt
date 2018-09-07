package com.appimake.drawerapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.da_menu_item.view.*

abstract class DrawerAppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var defaultContentBackground = Color.BLACK
    var defaultNavStyle = DANavBarStyle(Color.BLACK, "#FFFFFF", 18f)

    var navBG = Color.WHITE

    var selectedPosition = 0
    var menuItemSelectedBackgound = Color.CYAN

    abstract fun setUserProfile() : DAUserDataModel

    abstract fun setMenuItemList(ItemList: ArrayList<DAMenuItem>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setBackgroundColor(navBG)

        initData()
        initOnClick()
    }

    private fun initOnClick() {
        nav_view.da_profile_iv_picture.setOnClickListener {
            setUserProfile().onClick.onClick("Click Profile")
        }
    }

    private fun initData() {
        Glide.with(applicationContext).load(setUserProfile().pictureURL).into(nav_view.da_profile_iv_picture)
        nav_view.da_profile_tv_name.text = setUserProfile().name
        nav_view.da_profile_tv_mail.text = setUserProfile().email

        setMenu()
    }

    private fun setMenu(){
        val menuList = ArrayList<DAMenuItem>()
        setMenuItemList(menuList)

        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)

        menuList.forEach { daMenuItem ->
            var item: View = View(this)

            if(daMenuItem.customView is DAMenuItemDefault) {
                item = LayoutInflater.from(this).inflate(R.layout.da_menu_item, layout, false)

                item.da_menu_item_title.text = daMenuItem.customView.title

                setMenuIC(item.da_menu_item_icon, daMenuItem.customView.icDefault)
                setFontStyle(daMenuItem.customView.titleStyle, item.da_menu_item_title, false)


            } else if(daMenuItem.customView != null){
                item = daMenuItem.customView as View
            }

            item.tag = daMenuItem

            item.setOnClickListener { p0 -> selected(nav_view.da_menu_list.indexOfChild(p0)) }
            nav_view.da_menu_list.addView(item)
        }

        selected(selectedPosition)
    }

    private fun selected(position: Int){
        if(nav_view.da_menu_list.childCount > 0 &&
                (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).moduleView != null) {

            unSelect()
            selectedPosition = position
            if((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault){
                setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icSelected)
                setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, true)

                // Callback on select
                ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).let { daMenuItemDefault ->
                    if(daMenuItemDefault.onClick != null)
                        daMenuItemDefault.onClick.onClick(nav_view.da_menu_list.getChildAt(selectedPosition))
                }

            }
            nav_view.da_menu_list.getChildAt(position).setBackgroundColor(menuItemSelectedBackgound)


            setNavBar((nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).navBarStyle)
            title = (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).name

            navigation_app_bg.setBackgroundColor(defaultContentBackground)
            setContentBackground(navigation_app_bg, (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).background)

            val ft = this.supportFragmentManager.beginTransaction()
            ft.replace(R.id.da_container, (nav_view.da_menu_list.getChildAt(position).tag as DAMenuItem).moduleView!!).commit()

            if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun unSelect(){
        if(nav_view.da_menu_list.childCount > 0 &&
                (nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).moduleView != null) {
            nav_view.da_menu_list.getChildAt(selectedPosition).setBackgroundColor(navBG)

            if((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault){
                setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icDefault)
                setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, false)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setMenuIC(ic: ImageView, data: Any?){
        if(data != null) {
            if (data is String)
                Glide.with(this)
                        .load(data)
                        .into(ic)
            else if(data is Drawable)
                ic.setImageDrawable(data)
            else if(data is Bitmap)
                ic.setImageBitmap(data)
            else if(data is Int)
                ic.setImageResource(data)

        }
    }

    private fun setFontStyle(style: DATextStyle?, tv: TextView, isSelected: Boolean){
        style.let { daTextStyle ->
            if(daTextStyle != null){
                tv.textSize = daTextStyle.textSize
                if(isSelected)
                    tv.setTextColor(Color.parseColor(daTextStyle.selectedColor))
                else
                    tv.setTextColor(Color.parseColor(daTextStyle.defaultColor))
                tv.typeface = daTextStyle.typeFace
            }
        }
    }

    private fun setNavBar(navStyle: DANavBarStyle?){
        var style = defaultNavStyle
        if(navStyle != null)
            style = navStyle

        style.let {
            // Set NAV BG
            if(it.background is String)
                navigation_bar_bg.setBackgroundColor(Color.parseColor(it.background))
            else if(it.background is Int)
                navigation_bar_bg.setBackgroundColor(it.background)

            // Set Title
            navigation_bar_title.textSize = it.titleSize

            if(it.titleColor is String)
                navigation_bar_title.setTextColor(Color.parseColor(it.titleColor))
            else if(it.titleColor is Int)
                navigation_bar_title.setTextColor(it.titleColor)
        }
    }

    /**
     * String : Hex
     * Int : ResID
     * Drawable : drawable
     * */
    private fun setContentBackground(v: View, data: Any?){
        if(data != null) {
            if(data is Drawable)
                v.background = data
            else if(data is String)
                v.setBackgroundColor(Color.parseColor(data))
            else if(data is Int)
                v.setBackgroundResource(data)
        }
    }
}
