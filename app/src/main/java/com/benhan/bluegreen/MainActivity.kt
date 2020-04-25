package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tvRegister: TextView = findViewById(R.id.tvRegister)
        val tvLogin: TextView = findViewById(R.id.tvLogin)

        tvRegister.setOnClickListener {
            onRegisterClicked()
        }

        tvLogin.setOnClickListener {
            onLoginClicked()
        }





    }


    fun onRegisterClicked(){

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

    }

    fun onLoginClicked(){

        val intent = Intent(this, LoginActivity2::class.java)

        startActivity(intent)
    }
}
