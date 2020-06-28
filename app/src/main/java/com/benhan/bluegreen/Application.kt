package com.benhan.bluegreen

import android.app.Application

class MyApplication: Application() {

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var INSTANCE: MyApplication
        const val severUrl = "http://18.223.20.219/"

    }
}