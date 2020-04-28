package com.benhan.bluegreen

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.LoginFilter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity2 : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)




        val et_email = findViewById<EditText>(R.id.etEmail)
        val et_password = findViewById<EditText>(R.id.etPassword)


        val btn_login = findViewById<Button>(R.id.btnLogIn)
        val tvOnOtherWay:TextView = findViewById(R.id.tvLoginOtherWay)

        tvOnOtherWay.setOnClickListener {
            onOtherWayClicked()
        }


        btn_login.setOnClickListener {

        fun onClick(){

            val password = et_password.text.toString()
            var email = et_email.text.toString()

            val responseListener = Response.Listener<String> {

                fun onResponse(response: String) {


                    try {
                        val jsonObject = JSONObject(response)


                        val success: Boolean = jsonObject.getBoolean("success")
                        if (success){
                            var email = jsonObject.getString("email")
                            var password = jsonObject.getString("password")
                            Toast.makeText(applicationContext, "로그인 완료!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            intent.putExtra("email", email)
                            intent.putExtra("password", password)


                        } else {
                            Toast.makeText(applicationContext, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                            return


                        }
                    } catch (e: JSONException){
                        e.printStackTrace()
                    }


                }

            }
            val loginRequest = LoginRequest(email, password, responseListener)
            val queue = Volley.newRequestQueue(this)
            queue.add(loginRequest)
        }

        }

    }



    fun onOtherWayClicked(){

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

    }



}




