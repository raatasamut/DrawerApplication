package com.appimake.drawerapplication

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.appimake.drawerapp.*
import com.appimake.drawerapplication.module.FragmentA
import com.appimake.drawerapplication.module.FragmentB
import com.appimake.drawerapplication.module.FragmentC
import java.util.*

class MainActivity : DrawerAppActivity() {
    init {
        defaultContentBackground = Color.WHITE
        defaultNavStyle = DANavBarStyle("#FFDDAA", "#FFFFFF", 18f)

        navBG = Color.WHITE

        selectedPosition = 1
        menuItemSelectedBackgound = Color.CYAN
    }

    override fun setMenuItemList(ItemList: ArrayList<DAMenuItem>) {

        val headTextA = TextView(this)
        headTextA.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextA.typeface = Typeface.DEFAULT_BOLD
        headTextA.text = "Section A : Icon type"
        ItemList.add(DAMenuItem("TEST", headTextA, null, null, null))

        ItemList.add(DAMenuItem("EX 1",
                DAMenuItemDefault("IC is URL",
                        null,
                        "https://www.springers.com.au/wp-content/uploads/2018/06/if_facebook_2308066.png",
                        "https://png.pngtree.com/element_our/sm/20180301/sm_5a9794da1b10e.png",
                        null),
                null,
                null,
                FragmentA()))
        ItemList.add(DAMenuItem("EX 2",
                DAMenuItemDefault("IC is Drawable",
                        null,
                        resources.getDrawable(R.drawable.add, null),
                        resources.getDrawable(R.drawable.remove, null),
                        null),
                null,
                null,
                FragmentB()))
        ItemList.add(DAMenuItem("EX 3",
                DAMenuItemDefault("IC is Res ID",
                        null,
                        BitmapFactory.decodeResource(resources,
                                R.drawable.level),
                        BitmapFactory.decodeResource(resources,
                                R.drawable.settings),
                        null),
                null,
                null,
                FragmentC()))
        ItemList.add(DAMenuItem("EX 4",
                DAMenuItemDefault("IC is Bitmap",
                        null,
                        R.drawable.low_battery,
                        R.drawable.full_battery,
                        null),
                null,
                null,
                FragmentA()))


        val headTextB = TextView(this)
        headTextB.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextB.typeface = Typeface.DEFAULT_BOLD
        headTextB.text = "Section B : Action"
        ItemList.add(DAMenuItem("TEST", headTextB, null, null, null))

        ItemList.add(DAMenuItem("EX 5",
                DAMenuItemDefault("Callback action",
                        null,
                        null,
                        null,
                        object : DACallBack{
                            override fun onClick(data: Any) {
                                Toast.makeText(this@MainActivity, (data as View).tag.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }),
                null,
                null,
                FragmentA()))
        ItemList.add(DAMenuItem("EX 6",
                DAMenuItemDefault("Click and then change Title",
                        null,
                        null,
                        null,
                        object : DACallBack{
                            override fun onClick(data: Any) {
                                ((data as LinearLayout).getChildAt(1) as TextView).text = "Selected on " + Calendar.getInstance().timeInMillis
                            }
                        }),
                null,
                null,
                FragmentB()))


        val headTextC = TextView(this)
        headTextC.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextC.typeface = Typeface.DEFAULT_BOLD
        headTextC.text = "Section C : Text style"
        ItemList.add(DAMenuItem("TEST", headTextC, null, null, null))


        ItemList.add(DAMenuItem("EX 7",
                DAMenuItemDefault("Text size 24",
                        DATextStyle("#000000", "#000000", Typeface.DEFAULT, 24f),
                        null,
                        null,
                        null),
                null,
                null,
                FragmentA()))
        ItemList.add(DAMenuItem("EX 8",
                DAMenuItemDefault("Text color",
                        DATextStyle("#ff85a2", "#ffddd1", Typeface.DEFAULT, 14f),
                        null,
                        null,
                        null),
                null,
                null,
                FragmentA()))
        ItemList.add(DAMenuItem("EX 9",
                DAMenuItemDefault("Text typeface",
                        DATextStyle("#000000", "#000000", Typeface.DEFAULT_BOLD, 14f),
                        null,
                        null,
                        null),
                null,
                null,
                FragmentA()))

        val headTextD = TextView(this)
        headTextD.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextD.typeface = Typeface.DEFAULT_BOLD
        headTextD.text = "Section D : NavBar style"
        ItemList.add(DAMenuItem("TEST", headTextD,null,null, null))


        ItemList.add(DAMenuItem("EX 10",
                DAMenuItemDefault("Custom style",
                        null,
                        null,
                        null,
                        null),
                DANavBarStyle(Color.YELLOW, "#DDEEAA", 20f),
                null,
                FragmentA()))

        val headTextE = TextView(this)
        headTextE.setPadding(resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt(),
                0,
                resources.getDimension(R.dimen.nav_header_vertical_spacing).toInt())
        headTextE.typeface = Typeface.DEFAULT_BOLD
        headTextE.text = "Section E : More"
        ItemList.add(DAMenuItem("TEST", headTextE,null,null, null))

        ItemList.add(DAMenuItem("EX 11",
                DAMenuItemDefault("Change content background",
                        null,
                        null,
                        null,
                        null),
                DANavBarStyle(Color.TRANSPARENT, Color.BLACK, 18f),
                R.drawable.content_background,
                FragmentA()))

    }

    override fun setUserProfile() = DAUserDataModel(
            "Nattapong Rattasamut",
            "raatasamut@gmail.com",
            "https://thenypost.files.wordpress.com/2018/02/180223-stuffed-cat-feature-image.jpg",
            object : DACallBack{
                override fun onClick(data: Any) {
                    Toast.makeText(this@MainActivity, data.toString(), Toast.LENGTH_SHORT).show()
                }
            })

}
