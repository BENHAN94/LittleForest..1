package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.nhn.android.naverlogin.OAuthLogin

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val tvRegister = findViewById<TextView>(R.id.tv_register)

        tvRegister.setOnClickListener {


            onClick()

        }



    }

    fun onClick() {

        val intent = Intent(this, Register1::class.java)
        startActivity(intent)

    }


}
