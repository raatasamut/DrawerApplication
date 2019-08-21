package com.appimake.drawerapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.appimake.drawerapp.expand.DAExpandableLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.da_activity_main.*
import kotlinx.android.synthetic.main.da_activity_main.view.*
import kotlinx.android.synthetic.main.da_app_bar_main.*
import kotlinx.android.synthetic.main.da_badges.view.*
import kotlinx.android.synthetic.main.da_menu_item.view.*

abstract class DrawerAppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var defaultContentBackground = Color.BLACK
    var defaultNavStyle = DANavBarStyle(Color.BLACK, Color.WHITE, 18f, null, null)

    var drawerBG = Color.WHITE
    var drawerSubItemBG = Color.WHITE

    var menuItemSelectedBackground = Color.CYAN

    var fontTypeFace: Typeface? = null

    var selectedTag = ""

    private var selectedPosition = 0

    private var subMenuSelectedView: DASelectedView? = null

    private var moduleView: androidx.fragment.app.Fragment? = null

    abstract fun setDAHeader(): Any

    abstract fun setMenuItemList(ItemList: ArrayList<DAMenuItem>)

    abstract fun setDASelectedCallBack(): DACallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.da_activity_main)

        savedInstanceState?.getString(getString(R.string.selected_position))?.let {
            this.selectedTag = it
        }

        navigation_bar_menu.setOnClickListener {
            if (drawer_layout != null) {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setBackgroundColor(drawerBG)

        setHeader()
        setMenu()

        drawer_layout.addDrawerListener(object : androidx.drawerlayout.widget.DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {}

            override fun onDrawerSlide(p0: View, p1: Float) {}

            override fun onDrawerClosed(p0: View) {
                moduleView?.let {
                    supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.da_container, it)
                            .commit()
                }
            }

            override fun onDrawerOpened(p0: View) {
                subMenuSelectedView?.groupView?.performClick()
            }
        })

        selectModuleByTag(selectedTag)
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

    fun setFooter(view: View) {
        nav_view.da_footer.removeAllViews()
        nav_view.da_footer.addView(view)
    }

    private fun initOnClick() {
        nav_view.da_profile_iv_picture.setOnClickListener {
            (setDAHeader() as DAHeaderModel).onClick.onClick(resources.getString(R.string.da_profile_click))
        }
    }

    private fun initData() {
        if (fontTypeFace != null) {
            nav_view.da_profile_tv_name.typeface = fontTypeFace
            nav_view.da_profile_tv_mail.typeface = fontTypeFace
        }

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
                if (fontTypeFace != null) {
                    item.da_menu_item_title.typeface = fontTypeFace
                }
                item.da_menu_item_title.text = daMenuItem.customView.title
                setMenuIC(item.da_menu_item_icon, daMenuItem.customView.icDefault)
                setFontStyle(daMenuItem.customView.titleStyle, item.da_menu_item_title, false)
            } else if (daMenuItem.customView != null) {
                item = daMenuItem.customView as View
            }

            if (daMenuItem.badges != null && item is LinearLayout)
                setBadges(daMenuItem.badges, item, layout)

            item.tag = daMenuItem

            nav_view.da_menu_list.addView(item)

            // EXPANDED VIEW
            if (!daMenuItem.subMenuList.isEmpty()) {

                val exp = DAExpandableLayout(this)

                val subContainer = LinearLayout(this)
                subContainer.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
                subContainer.orientation = LinearLayout.VERTICAL
                subContainer.setBackgroundColor(drawerSubItemBG)

                exp.addView(subContainer)

                var expTag = ArrayList<String>()

                daMenuItem.subMenuList.forEachIndexed { index, sub ->

                    var subItem = LayoutInflater.from(this).inflate(R.layout.da_menu_item, layout, false)

                    if (sub.customView is DAMenuItemDefault) {
                        if (fontTypeFace != null) {
                            subItem.da_menu_item_title.typeface = fontTypeFace
                        }
                        subItem.da_menu_item_title.text = sub.customView.title
                        setMenuIC(subItem.da_menu_item_icon, sub.customView.icDefault)
                        setFontStyle(sub.customView.titleStyle, subItem.da_menu_item_title, false)
                    } else if (sub.customView != null) {
                        subItem = sub.customView as View
                    }

                    sub.tag?.let {
                        expTag.add(it)
                    }

                    subItem.tag = sub

                    subItem.setOnClickListener { p0 ->
                        Log.d("CSub", "${daMenuItem.title} : ${sub.title}")
                        selectedSubMenu(exp, subItem, daMenuItem, sub)
                    }

                    subContainer.addView(subItem)

                }

                exp.tag = expTag

                val arrow = ImageView(this)
                arrow.setImageDrawable(resources.getDrawable(R.drawable.arrow_down, null))

                (item as LinearLayout).addView(arrow)

                item.setOnClickListener { p0 ->
                    //                    val arr = (p0 as LinearLayout).getChildAt(p0.childCount -1)
//                    if (arr is ImageView)
                    arrow.animate().rotationXBy(180f).start()

                    if (exp.isCollapsed) {
                        exp.expand()
                    } else {
                        exp.collapse()
                    }
                }

                nav_view.da_menu_list.addView(exp)
            } else {
                item.setOnClickListener { p0 ->
                    selected(nav_view.da_menu_list.indexOfChild(p0))
                }
            }

        }

        selected(selectedPosition)
    }

    private fun clearSubMenuStyle(collapse: Boolean) {
        subMenuSelectedView?.let {
            it.view.setBackgroundColor(drawerSubItemBG)
            setMenuIC(it.view.da_menu_item_icon, (it.sub.customView as DAMenuItemDefault).icDefault)
            setFontStyle(it.sub.customView.titleStyle, it.view.da_menu_item_title, false)
            if (collapse && it.groupView.isExpanded) it.groupView.performClick()
        }
    }

    private fun selectedSubMenu(viewGroup: DAExpandableLayout, view: View, group: DAMenuItem, sub: DASubMenuItem) {
        clearSubMenuStyle(false)

        subMenuSelectedView = DASelectedView(viewGroup, view, group, sub)

        subMenuSelectedView?.sub?.let {
            if (it.title != null)
                setDASelectedCallBack().onClick(it)

            if (it.moduleView != null) {
                clearAction()
                unSelect()

                selectedTag = if (it.tag != null) it.tag else ""

                if (it.customView is DAMenuItemDefault) {
                    setMenuIC(view.da_menu_item_icon, it.customView.icSelected)
                    setFontStyle(it.customView.titleStyle, view.da_menu_item_title, true)

                    it.customView.let { daMenuItemDefault ->
                        daMenuItemDefault.onClick?.onClick(view)
                    }
                }
                view.setBackgroundColor(menuItemSelectedBackground)
                setNavBar(group.navBarStyle)
                setFMNavTitle(it.title)

                navigation_app_bg.setBackgroundColor(defaultContentBackground)
                setContentBackground(navigation_app_bg, group.background)

                moduleView = it.moduleView

                if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
            }
        }
    }

    fun selectModuleByTag(tag: String, forceChange: Boolean = false) {
        loop@ for (i in 0 until nav_view.da_menu_list.childCount) {
            if (nav_view.da_menu_list.getChildAt(i).tag is DAMenuItem) {
                if ((nav_view.da_menu_list.getChildAt(i).tag as DAMenuItem).tag.equals(tag)) {
                    selected(i, forceChange)
                    return
                }

                (nav_view.da_menu_list.getChildAt(i).tag as DAMenuItem).subMenuList.forEach {
                    if (tag.equals(it.tag)) {
                        findViewByTag(tag)?.let { subView: View ->
                            selectedSubMenu(subView.parent.parent as DAExpandableLayout, subView, (nav_view.da_menu_list.getChildAt(i).tag as DAMenuItem), it)
                        }
                    }
                }
            }
        }
    }

    private fun findViewByTag(tag: String): View? {
        for (i in 0 until nav_view.da_menu_list.childCount) {
            if (nav_view.da_menu_list.getChildAt(i) is DAExpandableLayout) {
                val vtmp = (nav_view.da_menu_list.getChildAt(i) as DAExpandableLayout).getChildAt(0) as LinearLayout
                for (j in 0 until vtmp.childCount) {
                    if (vtmp.getChildAt(j).tag is DASubMenuItem) {
                        vtmp.getChildAt(j).tag.let {
                            if ((it as DASubMenuItem).tag.equals(tag)) {
                                return vtmp.getChildAt(j)
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun selected(position: Int, forceChange: Boolean = false) {
        if (nav_view.da_menu_list.childCount > 0 &&
                nav_view.da_menu_list.getChildAt(position) != null) {

            val item = nav_view.da_menu_list.getChildAt(position).tag

            if (item is DAMenuItem) if (item.subMenuList.isEmpty())
                item.let { menuItem: DAMenuItem ->
                    if (menuItem.title != null)
                        setDASelectedCallBack().onClick(menuItem)
                    if (menuItem.moduleView != null) {
                        clearAction()
                        unSelect()
                        clearSubMenuStyle(true)
                        selectedPosition = position
                        selectedTag = if (menuItem.tag != null) menuItem.tag else ""
                        if ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault) {
                            setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icSelected)
                            setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, true)

                            ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).let { daMenuItemDefault ->
                                daMenuItemDefault.onClick?.onClick(nav_view.da_menu_list.getChildAt(selectedPosition))
                            }

                        }
                        nav_view.da_menu_list.getChildAt(position).setBackgroundColor(menuItemSelectedBackground)

                        setNavBar(menuItem.navBarStyle)

                        setFMNavTitle(menuItem.title)

                        navigation_app_bg.setBackgroundColor(defaultContentBackground)
                        setContentBackground(navigation_app_bg, menuItem.background)

                        if (moduleView == null) {
                            supportFragmentManager
                                    .beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.da_container, menuItem.moduleView)
                                    .commit()
                        }
                        moduleView = menuItem.moduleView

                        if (forceChange) {
                            moduleView?.let {
                                supportFragmentManager
                                        .beginTransaction()
                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .replace(R.id.da_container, it)
                                        .commit()
                            }
                        }

                        subMenuSelectedView = null

                        if (drawer_layout != null && drawer_layout.isDrawerOpen(GravityCompat.START)) {
                            drawer_layout.closeDrawer(GravityCompat.START)
                        }
                    }
                }
        }
    }

    private fun unSelect() {
        if (nav_view.da_menu_list.childCount > 0)
            if (nav_view.da_menu_list.getChildAt(selectedPosition).tag is DAMenuItem) {
                (nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).moduleView?.let {
                    nav_view.da_menu_list.getChildAt(selectedPosition).setBackgroundColor(drawerBG)

                    if ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView is DAMenuItemDefault) {
                        setMenuIC(nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_icon, ((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).icDefault)
                        setFontStyle(((nav_view.da_menu_list.getChildAt(selectedPosition).tag as DAMenuItem).customView as DAMenuItemDefault).titleStyle, nav_view.da_menu_list.getChildAt(selectedPosition).da_menu_item_title, false)
                    }
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

                    if (daTextStyle.typeFace != null)
                        tv.typeface = daTextStyle.typeFace
                    else if (fontTypeFace != null) {
                        tv.typeface = fontTypeFace
                    }
                }
            }
        }
    }

    private fun setNavBar(navStyle: DANavBarStyle?) {
        var style = defaultNavStyle

        navStyle?.let {
            style = it
        }
        if (style.background == -1) {
            navigation_bar_bg.visibility = View.GONE
        } else {
            navigation_bar_bg.visibility = View.VISIBLE
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
                acc.removeAllViews()
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


        if (fontTypeFace != null) {
            badgesView.da_badges_count.typeface = fontTypeFace
        }
        badgesView.da_badges_count.text = badges.count

        badges.onChange.onChange(badgesView.da_badges_count)

        parent.addView(badgesView)
    }


    private fun setFMNavTitle(data: Any?) {
        when {
            data != null -> when (data) {
                is Drawable -> {
                    usingTitle(false)
                    navigation_bar_title.visibility = View.GONE
                    navigation_bar_title_iv.visibility = View.VISIBLE
                    navigation_bar_title_iv.setImageDrawable(data)
                }
                is String -> {
                    usingTitle(false)
                    navigation_bar_title_iv.visibility = View.GONE
                    navigation_bar_title.visibility = View.VISIBLE
                    navigation_bar_title.text = data.toString()
                    if (fontTypeFace != null)
                        navigation_bar_title.typeface = fontTypeFace
                }
                is Int -> {
                    usingTitle(false)
                    navigation_bar_title.visibility = View.GONE
                    navigation_bar_title_iv.visibility = View.VISIBLE
                    navigation_bar_title_iv.setImageResource(data)
                }
                is View -> {
                    usingTitle(true)
                    navigation_bar_title_custom_view.removeAllViews()
                    navigation_bar_title_custom_view.addView(data)
                }
            }
        }
    }

    private fun usingTitle(isCustom: Boolean) {
        if (isCustom) {
            navigation_bar_title_normal_view.visibility = View.GONE
            navigation_bar_title_custom_view.visibility = View.VISIBLE
        } else {
            navigation_bar_title_normal_view.visibility = View.VISIBLE
            navigation_bar_title_custom_view.visibility = View.GONE
        }
    }

//    override fun onSaveInstanceState(outState: Bundle?) {
//        outState!!.putString(getString(R.string.selected_position), this.selectedTag)
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        savedInstanceState!!.getString(getString(R.string.selected_position))?.let {
//            this.selectedTag = it
//        }
//    }
}