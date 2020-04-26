package com.benhan.bluegreen

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.LoginFilter
import android.widget.EditText
import android.widget.TextView

    class LoginActivity2 : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)






        val tvOnOtherWay:TextView = findViewById(R.id.tvLoginOtherWay)

        tvOnOtherWay.setOnClickListener {
            onOtherWayClicked()
        }


    }



    fun onOtherWayClicked(){

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

    }



}




