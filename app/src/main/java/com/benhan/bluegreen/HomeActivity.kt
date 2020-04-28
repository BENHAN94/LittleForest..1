package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intent: Intent = getIntent()

        val tv_email = findViewById<TextView>(R.id.tv_email)
        val tv_password = findViewById<TextView>(R.id.tv_password)
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")


        tv_email.setText(email)
        tv_password.setText(password)



    }
}
