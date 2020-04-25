package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val hd = Handler()
        hd.postDelayed(object: Runnable{
            override fun run(){

                startActivity(Intent(application, MainActivity::class.java))
                finish()
            }
        }, 3000)

    }


    override fun onBackPressed() {

    }


}

