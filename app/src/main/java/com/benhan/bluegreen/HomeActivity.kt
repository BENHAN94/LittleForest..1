package com.benhan.bluegreen

import android.app.Activity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val imageSelector: GridView = findViewById(R.id.imageSelector)
        val ivPlus: ImageView = findViewById(R.id.plus)


        imageSelector.numColumns = 4
        imageSelector.horizontalSpacing = 15
        imageSelector.verticalSpacing = 15
        imageSelector.stretchMode = GridView.STRETCH_COLUMN_WIDTH

    }


}























