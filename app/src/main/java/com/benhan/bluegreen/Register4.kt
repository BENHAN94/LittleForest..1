package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Register4 : AppCompatActivity() {

    val TAG: String = "로그"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register4)


        //

        val birthday = intent.getStringExtra("birthday")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")



        //

        val etNickname = findViewById<EditText>(R.id.etNickNameRegister)
        val nicknameChar = etNickname.text


        //button

        val btnRegister = findViewById<Button>(R.id.btnRegisterRegister)
        btnRegister.isEnabled = false

        fun btnStyle() {

            if (btnRegister.isEnabled) {

                btnRegister.setBackgroundResource(R.drawable.button_shape)
                val enabledTextColor = ContextCompat.getColor(this@Register4, R.color.background)
                btnRegister.setTextColor(enabledTextColor)

            } else if (!btnRegister.isEnabled) {


                btnRegister.isEnabled = false
                btnRegister.setBackgroundResource(R.drawable.button_shape_disable)
                val disabledTextColor = ContextCompat.getColor(this@Register4, R.color.disabled)
                btnRegister.setTextColor(disabledTextColor)

            }


        }

        btnStyle()

        //


        etNickname.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(nicknameChar.isNotEmpty()){

                    btnRegister.isEnabled = true
                    btnStyle()

                    if (nicknameChar.length > 2){
                        
                        Log.d(TAG, "Register4 - onTextChanged() called")



                        btnRegister.setOnClickListener(object : View.OnClickListener{

                            override fun onClick(v: View?) {
                                
                                Log.d(TAG, "Register4 - onClick() called")


                                val nickName = etNickname.text.toString()

                                val responseListener = object : Response.Listener<String>{


                                    override fun onResponse(response: String?) {
                                        
                                        Log.d(TAG, "Register4 - onResponse() called")


                                        try {

                                            val jsonObject = JSONObject(response)
                                            val success: Boolean = jsonObject.getBoolean("success")
                                            if(success){

                                                Toast.makeText(
                                                    applicationContext, "회원가입 완료!", Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this@Register4, LoginActivity2::class.java)
                                                startActivity(intent)

                                            } else{

                                                return

                                            }

                                        } catch (e: JSONException){
                                            e.printStackTrace()
                                        }
                                    }


                                }

                                val registerRequest = RegisterRequest(email, password, nickName, birthday, responseListener)
                                val queue = Volley.newRequestQueue(this@Register4)
                                queue.add(registerRequest)

                            }

                        })

                    }

                }else{

                    btnRegister.isEnabled = false
                    btnStyle()
                }

            }

            override fun afterTextChanged(s: Editable?) {

                if(nicknameChar.isNotEmpty()){

                    btnRegister.isEnabled = true
                    btnStyle()

                    if (nicknameChar.length > 2){

                        Log.d(TAG, "Register4 - afterTextChanged() called")

                        btnRegister.setOnClickListener(object : View.OnClickListener{

                            override fun onClick(v: View?) {
                                
                                Log.d(TAG, "Register4 - onClick() called")


                                val nickName = etNickname.text.toString()

                                val responseListener = object : Response.Listener<String>{


                                    override fun onResponse(response: String?) {
                                        
                                        Log.d(TAG, "Register4 - onResponse() called")


                                        try {

                                            val jsonObject = JSONObject(response)
                                            val success: Boolean = jsonObject.getBoolean("success")
                                            if(success){

                                                Toast.makeText(
                                                    applicationContext, "회원가입 완료!", Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this@Register4, LoginActivity2::class.java)
                                                intent.putExtra("email", email)
                                                startActivity(intent)

                                            } else{

                                                return

                                            }

                                        } catch (e: JSONException){
                                            e.printStackTrace()
                                        }
                                    }


                                }


                                    val registerRequest = RegisterRequest(
                                        email,
                                        password,
                                        nickName,
                                        birthday,
                                        responseListener
                                    )
                                    val queue = Volley.newRequestQueue(this@Register4)
                                    queue.add(registerRequest)

                            }

                        })

                    }

                } else{

                    btnRegister.isEnabled = false
                    btnStyle()
                }

            }



        })


    }

}
