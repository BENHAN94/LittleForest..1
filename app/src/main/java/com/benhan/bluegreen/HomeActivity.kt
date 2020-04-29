package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvPassword = findViewById<TextView>(R.id.tv_password)


//        val intent: Intent = intent

        if (intent.hasExtra("email")){

            tvEmail.text = intent.getStringExtra("email")

        } else {
            Toast.makeText(this, "전달된 이메일이 없습니다", Toast.LENGTH_SHORT).show()
        }

        if (intent.hasExtra("password")){

            tvPassword.text = intent.getStringExtra("password")

        } else {
            Toast.makeText(this, "전달된 비밀번호가 없습니다", Toast.LENGTH_SHORT).show()
        }

    }
}
