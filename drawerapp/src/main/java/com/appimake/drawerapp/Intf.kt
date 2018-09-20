package com.appimake.drawerapp

import android.widget.TextView


interface DACallBack {
    fun onClick(data: Any)
}

interface DABadgesChange {
    fun onChange(view: TextView)
}