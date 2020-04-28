package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Register : AppCompatActivity() {


    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        
        Log.d(TAG, "Register - onCreate() called")


        val et_email = findViewById<EditText>(R.id.etEmail)
        val et_password = findViewById<EditText>(R.id.etPassword)
        val et_nickname = findViewById<EditText>(R.id.etNickName)
        val tv_birthday = findViewById<EditText>(R.id.select_birthday)



        val register_btn = findViewById<Button>(R.id.btnRegister)



        register_btn.setOnClickListener (View.OnClickListener(){



            @Override
            fun onClick(view: View) {


                Log.d(TAG, "Register - onNextButtonClicked() called")

                val email = et_email.text.toString()
                val password = et_password.text.toString()
                val nickname = et_nickname.text.toString()
                val birthday = tv_birthday.text.toString()

                val responseListener = Response.Listener<String>() {

                    @Override
                    fun onResponse(response: String) {


                        try {
                            val jsonObject = JSONObject(response)
                            val success: Boolean = jsonObject.getBoolean("success")
                            if (success){
                                Toast.makeText(applicationContext, "이메일 등록 완료!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Register, LoginActivity2::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(applicationContext, "이메일을 확인해주세요.",Toast.LENGTH_SHORT).show()
                                return
                            }
                        } catch (e: JSONException){
                            e.printStackTrace()
                        }


                    }





                }


                //서버로 volley를 이용해 요청함.
                val registerRequest = RegisterRequest(email, password, nickname, birthday, responseListener)
                val queue = Volley.newRequestQueue(this)
                queue.add(registerRequest)



            }




        })











    }





}
