package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Response
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

class Register2 : AppCompatActivity() {


    companion object{

        fun isValidEmail(email: CharSequence): Boolean{

            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)






        val birthday = intent.getStringExtra("birthday")


        val etEmail = findViewById<EditText>(R.id.etEmailRegister)

        val email = etEmail.text



        val btnNext = findViewById<Button>(R.id.btnNext)

        btnNext.isEnabled = false


        fun btnStyle() {

            if (btnNext.isEnabled) {

                btnNext.setBackgroundResource(R.drawable.button_shape)
                val enabledTextColor = ContextCompat.getColor(this@Register2, R.color.background)
                btnNext.setTextColor(enabledTextColor)

            } else if (btnNext.isEnabled === false) {


                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.drawable.button_shape_disable)
                val disabledTextColor = ContextCompat.getColor(this@Register2, R.color.disabled)
                btnNext.setTextColor(disabledTextColor)

            }

        }















        etEmail.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (email.isNotEmpty()){

                    btnNext.isEnabled = true
                    btnStyle()

                    if (isValidEmail(email)) {

                        btnNext.setOnClickListener {
                            fun onClick() {

                                val intent = Intent(this@Register2, Register3::class.java)

                                intent.putExtra("birtday", birthday)
                                intent.putExtra("email", email.toString())
                                startActivity(intent)
                            }
                            onClick()
                        }

                    }
                    else {



                        btnNext.setOnClickListener{


                            Toast.makeText(this@Register2, "이메일주소를 확인해주세요", Toast.LENGTH_SHORT).show()


                        }
                    }

                }

                else{

                    btnNext.isEnabled = false

                    btnStyle()

                }



            }


            override fun afterTextChanged(s: Editable?) {



                if (email.isNotEmpty()){

                    btnNext.isEnabled = true
                    btnStyle()

                    if (isValidEmail(email)) {

                        btnNext.setOnClickListener {

                            fun onClick() {

                                val intent = Intent(this@Register2, Register3::class.java)

                                intent.putExtra("birtday", birthday)
                                intent.putExtra("email", email.toString())
                                startActivity(intent)
                            }
                            onClick()
                        }

                    }

                    else {

                        btnNext.setOnClickListener{

                            Toast.makeText(this@Register2, "이메일주소를 확인해주세요", Toast.LENGTH_SHORT).show()


                        }

                    }
                }
                else if (email.isEmpty()){

                    btnNext.isEnabled = false

                    btnStyle()



                }


            }





        })





    }





}
