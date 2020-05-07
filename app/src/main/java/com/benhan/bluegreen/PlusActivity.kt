package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class PlusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus)


        val colorEnabled = ContextCompat.getColor(this, R.color.background)
        val colorDisabled = ContextCompat.getColor(this, R.color.navi)

        val fragmentGallery = PlusFragmentGallery()
        val fragmentPhoto = PlusFragmentPhoto()
        val fragmentVideo = PlusFragmentVideo()

        val gallery = findViewById<TextView>(R.id.gallery)
        val photo = findViewById<TextView>(R.id.photo)
        val video = findViewById<TextView>(R.id.video)
        val ivX = findViewById<ImageView>(R.id.ivX)

        ivX.setOnClickListener {

            finish()
        }

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.plusFrame, fragmentGallery)
            .commitAllowingStateLoss()

        fun clickHandler(view: TextView){

            view.setOnClickListener {

                val fTransaction = fragmentManager.beginTransaction()

                when(view.id){

                    R.id.gallery -> {

                        fTransaction.replace(R.id.plusFrame, fragmentGallery)
                        gallery.setTextColor(colorEnabled)
                        photo.setTextColor(colorDisabled)
                        video.setTextColor(colorDisabled)

                    }

                    R.id.photo -> {

                        fTransaction.replace(R.id.plusFrame, fragmentPhoto)
                        gallery.setTextColor(colorDisabled)
                        photo.setTextColor(colorEnabled)
                        video.setTextColor(colorDisabled)

                    }

                    R.id.video -> {

                        fTransaction.replace(R.id.plusFrame, fragmentVideo)
                        gallery.setTextColor(colorDisabled)
                        photo.setTextColor(colorDisabled)
                        video.setTextColor(colorEnabled)

                    }


                }

                fTransaction.commit()


            }


        }

        clickHandler(gallery)
        clickHandler(photo)
        clickHandler(video)




    }
}
