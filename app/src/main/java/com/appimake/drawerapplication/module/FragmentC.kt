package com.appimake.drawerapplication.module

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appimake.drawerapplication.R


class FragmentC : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fm = inflater.inflate(R.layout.test_fm, container, false)
        fm.setBackgroundColor(Color.BLACK)
        return fm
    }
}