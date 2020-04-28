package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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


        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etNickname = findViewById<EditText>(R.id.etNickName)
        val tvBirthday = findViewById<EditText>(R.id.select_birthday)
        val registerBtn = findViewById<Button>(R.id.btnRegister)




//        var registerBtn = object: View.OnClickListener

        registerBtn.setOnClickListener(object: View.OnClickListener {

            override fun onClick(v: View){


                Log.d(TAG, "Register - onClick() called")

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val nickname = etNickname.text.toString()
                val birthday = tvBirthday.text.toString()

                val responseListener = object: Response.Listener<String>{


                    override fun onResponse(response: String) {


                        try {

                            val jsonObject = JSONObject(response)
                            val success: Boolean = jsonObject.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    applicationContext,
                                    "회원가입 등록 완료!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@Register, LoginActivity2::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "다시 한번 확인해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }


                    }





                }


                //서버로 volley를 이용해 요청함.
                val registerRequest = RegisterRequest(email, password, nickname, birthday, responseListener)
                val queue = Volley.newRequestQueue(this@Register)
                queue.add(registerRequest)



            }

        })
























    }





}
