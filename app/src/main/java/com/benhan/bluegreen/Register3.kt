package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

class Register3 : AppCompatActivity() {


    companion object{

        fun isValidPassword(password: CharSequence): Boolean {

            val passwordPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{9,12}$"
            return Pattern.compile(passwordPattern).matcher(password).matches()


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register3)


        val birthday = intent.getStringExtra("birthday")
        val email = intent.getStringExtra("email")
        val etPaswword = findViewById<EditText>(R.id.etPasswordRegister)
        val passwordChar = etPaswword.text
        val btnNext = findViewById<Button>(R.id.btnNext)


        btnNext.isEnabled = false

        fun btnStyle() {

            if (btnNext.isEnabled) {

                btnNext.setBackgroundResource(R.drawable.button_shape)
                val enabledTextColor = ContextCompat.getColor(this@Register3, R.color.background)
                btnNext.setTextColor(enabledTextColor)

            } else if (!btnNext.isEnabled) {


                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.drawable.button_shape_disable)
                val disabledTextColor = ContextCompat.getColor(this@Register3, R.color.disabled)
                btnNext.setTextColor(disabledTextColor)

            }

        }

        etPaswword.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (passwordChar.length >= 6){

                    btnNext.isEnabled = true
                    btnStyle()

                    if(isValidPassword(passwordChar)){

                        btnNext.setOnClickListener {

                            fun onClick(){

                                val intent = Intent(this@Register3, Register4::class.java)
                                intent.putExtra("birthday", birthday)
                                intent.putExtra("email", email)
                                intent.putExtra("password", passwordChar.toString())
                                startActivity(intent)


                            }

                            onClick()

                        }

                    }

                    else{
                        btnNext.setOnClickListener {
                            Toast.makeText(
                                this@Register3,
                                "영문, 숫자, 특수문자를 모두 쓰셨나요?",
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                    }


                }

                else {

                    btnNext.isEnabled = false
                    btnStyle()
                }

            }

            override fun afterTextChanged(s: Editable?) {


                if (passwordChar.length >= 6){

                    btnNext.isEnabled = true
                    btnStyle()

                    if(isValidPassword(passwordChar)){

                        btnNext.setOnClickListener {

                            fun onClick(){

                                val intent = Intent(this@Register3, Register4::class.java)
                                intent.putExtra("birthday", birthday)
                                intent.putExtra("email", email)
                                intent.putExtra("password", passwordChar.toString())
                                startActivity(intent)


                            }

                            onClick()

                        }

                    }

                    else{


                        btnNext.setOnClickListener {
                            Toast.makeText(
                                this@Register3,
                                "영문, 숫자, 특수문자를 모두 쓰셨나요?",
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                    }


                }

                else {

                    btnNext.isEnabled = false
                    btnStyle()
                }

            }

            })





        }





    }

