package com.benhan.bluegreen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity2 : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)



        val emailFromRegister4 = intent.getStringExtra("email")


        val et_email = findViewById<EditText>(R.id.etEmailLogin)
        val et_password = findViewById<EditText>(R.id.etPasswordLogin)


        if (!emailFromRegister4.isNullOrEmpty()){

            et_email.text = Editable.Factory.getInstance().newEditable(emailFromRegister4)
        }

        val btn_login = findViewById<Button>(R.id.btnLogIn)
        val tvOnOtherWay:TextView = findViewById(R.id.tvLoginOtherWay)

        tvOnOtherWay.setOnClickListener {
            onOtherWayClicked()
        }


        btn_login.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View){

                var password = et_password.text.toString()
                var email = et_email.text.toString()

                val responseListener = object: Response.Listener<String> {

                    override fun onResponse(response: String) {


                        try {
                            val jsonObject = JSONObject(response)


                            val success: Boolean = jsonObject.getBoolean("success")
                            if (success){
                                email = jsonObject.getString("email")
                                password = jsonObject.getString("password")
                                Toast.makeText(applicationContext, "로그인 완료!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity2, HomeActivity::class.java)

                                intent.putExtra("email", email)
                                intent.putExtra("password", password)
                                startActivity(intent)


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
                val queue = Volley.newRequestQueue(this@LoginActivity2)
                queue.add(loginRequest)
            }


        } )





    }



    private fun onOtherWayClicked(){

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

    }



}




