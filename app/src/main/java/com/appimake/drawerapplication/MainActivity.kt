package com.appimake.drawerapplication

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.appimake.drawerapp.*
import com.appimake.drawerapplication.module.FragmentA
import com.appimake.drawerapplication.module.FragmentB
import com.appimake.drawerapplication.module.FragmentC
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : DrawerAppActivity() {
    override fun setDASelectedCallBack(): DACallBack = object : DACallBack {
        override fun onClick(data: Any) {
            Log.d("DACallback", data.toString())
        }
    }

    init {
        defaultContentBackground = Color.WHITE
        defaultNavStyle = DANavBarStyle()

        drawerBG = Color.LTGRAY
        drawerSubItemBG = Color.WHITE
        menuItemSelectedBackground = Color.CYAN

        selectedTag = "EX 1"
    }

    override fun setDAHeader() = DAHeaderModel(
            "Nattapong Rattasamut",
            "raatasamut@gmail.com",
            "https://thenypost.files.wordpress.com/2018/02/180223-stuffed-cat-feature-image.jpg",
            null,
            object : DACallBack {
                override fun onClick(data: Any) {
                    Toast.makeText(this@MainActivity, data.toString(), Toast.LENGTH_SHORT).show()

                }
            })

    override fun setMenuItemList(ItemList: ArrayList<DAMenuItem>) {

        val headTextA = TextView(this)
        headTextA.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextA.typeface = Typeface.DEFAULT_BOLD
        headTextA.text = "Section A : Icon type"
        ItemList.add(DAMenuItem(tag = "TEST 1", title = "TEST", customView = headTextA))

        ItemList.add(DAMenuItem(
                tag = "EX 1",
                title = "EX 1",
                customView = DAMenuItemDefault(
                        title = "IC is URL",
                        icDefault = "https://www.springers.com.au/wp-content/uploads/2018/06/if_facebook_2308066.png",
                        icSelected = "https://png.pngtree.com/element_our/sm/20180301/sm_5a9794da1b10e.png"),
                moduleView = FragmentA()))
        ItemList.add(DAMenuItem(
                tag = "EX 2",
                title = "EX 2",
                customView = DAMenuItemDefault(title = "IC is Drawable",
                        icDefault = resources.getDrawable(R.drawable.add, null),
                        icSelected = resources.getDrawable(R.drawable.remove, null)),
                moduleView = FragmentB()))
        ItemList.add(DAMenuItem("EX 3", "EX 3",
                DAMenuItemDefault(
                        title = "IC is Bitmap",
                        icDefault = BitmapFactory.decodeResource(resources,
                                R.drawable.level),
                        icSelected = BitmapFactory.decodeResource(resources,
                                R.drawable.settings)),
                moduleView = FragmentC()))
        ItemList.add(DAMenuItem("EX 4", "EX 4",
                DAMenuItemDefault(
                        title = "IC is Res ID",
                        icDefault = R.drawable.low_battery,
                        icSelected = R.drawable.full_battery),
                moduleView = FragmentA()))

        val headTextB = TextView(this)
        headTextB.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextB.typeface = Typeface.DEFAULT_BOLD
        headTextB.text = "Section B : Action"
        ItemList.add(DAMenuItem(tag = "TEST 2", title = "TEST", customView = headTextB))

        ItemList.add(DAMenuItem(
                tag = "EX 5",
                title = "EX 5",
                customView = DAMenuItemDefault(
                        title = "Callback action",
                        onClick = object : DACallBack {
                            override fun onClick(data: Any) {
                                Toast.makeText(this@MainActivity, (data as View).tag.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }),
                moduleView = FragmentA()))
        ItemList.add(DAMenuItem(
                tag = "EX 6",
                title = "EX 6",
                customView = DAMenuItemDefault(title = "Click and then change Title",
                        onClick = object : DACallBack {
                            override fun onClick(data: Any) {
                                ((data as LinearLayout).getChildAt(1) as TextView).text = "Selected on " + Calendar.getInstance().timeInMillis
                            }
                        }),
                moduleView = FragmentB()))

        val headTextC = TextView(this)
        headTextC.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextC.typeface = Typeface.DEFAULT_BOLD
        headTextC.text = "Section C : Text style"
        ItemList.add(DAMenuItem(tag = "TEST 3", title = "TEST", customView = headTextC))


        ItemList.add(DAMenuItem(tag = "EX 7", title = "EX 7",
                customView = DAMenuItemDefault(
                        title = "Text size 24",
                        titleStyle = DATextStyle("#000000", "#000000", Typeface.DEFAULT, 24f)),
                moduleView = FragmentA()))
        ItemList.add(DAMenuItem(
                tag = "EX 8",
                title = "EX 8",
                customView = DAMenuItemDefault(title = "Text color",
                        titleStyle = DATextStyle("#ff85a2", "#ffddd1", Typeface.DEFAULT, 14f)),
                moduleView = FragmentA()))
        ItemList.add(DAMenuItem("EX 9", "EX 9",
                DAMenuItemDefault(
                        title = "Text typeface",
                        titleStyle = DATextStyle("#000000", "#000000", Typeface.DEFAULT_BOLD, 14f)),
                moduleView = FragmentA()))

        val headTextD = TextView(this)
        headTextD.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextD.typeface = Typeface.DEFAULT_BOLD
        headTextD.text = "Section D : NavBar style"
        ItemList.add(DAMenuItem(tag = "TEST 4", title = "TEST", customView = headTextD))

        ItemList.add(DAMenuItem(
                tag = "EX 10",
                title = "EX 10",
                customView = DAMenuItemDefault(title = "Custom style"),
                navBarStyle = DANavBarStyle(background = Color.YELLOW, titleColor = "#DDEEAA", titleSize = 20f),
                moduleView = FragmentA()))


        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)

        var exView = LayoutInflater.from(this).inflate(com.appimake.drawerapp.R.layout.da_badges, layout, false)


        ItemList.add(DAMenuItem(
                tag = "EX 11",
                title = "EX 11",
                customView = DAMenuItemDefault("Action",
                        null,
                        null,
                        null,
                        null),
                navBarStyle = DANavBarStyle(
                        Color.BLUE,
                        "#FFFFFF",
                        18f,
                        DAActionItem(R.drawable.low_battery, exView, object : DACallBack {
                            override fun onClick(data: Any) {
                                Toast.makeText(this@MainActivity, "click primary action", Toast.LENGTH_SHORT).show()
                            }
                        }),
                        DAActionItem(R.drawable.settings, null, object : DACallBack {
                            override fun onClick(data: Any) {
                                Toast.makeText(this@MainActivity, "click secondary action", Toast.LENGTH_SHORT).show()
                            }
                        })),
                moduleView = FragmentA()))

        val headTextE = TextView(this)
        headTextE.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextE.typeface = Typeface.DEFAULT_BOLD
        headTextE.text = "Section E : More"
        ItemList.add(DAMenuItem(tag = "TEST 5", title = "TEST", customView = headTextE))

        ItemList.add(DAMenuItem("EX 11.1", resources.getDrawable(R.drawable.add, null),
                DAMenuItemDefault(title = "Change content background"),
                null,
                DANavBarStyle(Color.TRANSPARENT, Color.BLACK, 18f, null, null),
                R.drawable.content_background,
                FragmentA()))

        ItemList.add(DAMenuItem("EX 11.2",
                SearchView(applicationContext),
                DAMenuItemDefault(title = "Title as View"),
                null,
                DANavBarStyle(Color.TRANSPARENT, Color.BLACK, 18f, null, null),
                R.drawable.content_background,
                FragmentA()))

        ItemList.add(DAMenuItem(
                tag = "EX 11.3",
                customView = DAMenuItemDefault(title = "Hide action bar"),
                navBarStyle = DANavBarStyle(background = -1),
                moduleView = FragmentA()))

        ItemList.add(DAMenuItem(
                tag = "EX 11.4",
                title = DAMenuType.GROUP,
                customView = DAMenuItemDefault(title = "With sub menu"),
                subMenuList = ArrayList<DASubMenuItem>().apply {
                    for (i in 0..5) {
                        add(DASubMenuItem(
                                tag = "sub_$i",
                                title = "Sub $i",
                                customView = DAMenuItemDefault(
                                        title = "Sub $i",
                                        icDefault = BitmapFactory.decodeResource(resources,
                                                R.drawable.level),
                                        icSelected = BitmapFactory.decodeResource(resources,
                                                R.drawable.settings)),
                                moduleView = FragmentA()))
                    }
                }))

        val headTextF = TextView(this)
        headTextF.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextF.typeface = Typeface.DEFAULT_BOLD
        headTextF.text = "Section F : Badges"
        ItemList.add(DAMenuItem(tag = "TEST 6", title = "TEST", customView = headTextF))

        ItemList.add(DAMenuItem("EX 13", "EX 13",
                DAMenuItemDefault(title = "Badges"),
                DABadges("14", Color.RED, Color.WHITE, 5, GradientDrawable.OVAL, object : DABadgesChange {
                    override fun onChange(view: TextView) {
                        view.text = "33"
                    }
                }),
                null,
                null,
                FragmentA()))
    }
}