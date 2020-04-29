package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.Volley
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







        val checkSign = findViewById<ImageView>(R.id.check)

        val warningSign = findViewById<ImageView>(R.id.warning)



        checkSign.visibility = View.INVISIBLE
        warningSign.visibility = View.INVISIBLE





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

            } else if (!btnNext.isEnabled) {


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





            }


            override fun afterTextChanged(s: Editable?) {


                if (email.isNotEmpty()){

                    btnNext.isEnabled = true
                    btnStyle()

                    if (isValidEmail(email)) {


                        val responseListener = object: Response.Listener<String> {


                            override fun onResponse(response: String?) {


                                try {

                                    val jsonResponse = JSONObject(response)
                                    val success: Boolean = jsonResponse.getBoolean("success")
                                    if(success){


                                        checkSign.visibility = View.VISIBLE
                                        warningSign.visibility = View.INVISIBLE

                                        btnNext.isEnabled = true
                                        btnStyle()


                                    } else{


                                        checkSign.visibility = View.INVISIBLE
                                        warningSign.visibility = View.VISIBLE

                                        btnNext.isEnabled = false
                                        btnStyle()




                                    }


                                } catch (e: JSONException){

                                    e.printStackTrace()
                                }

                            }

                        }

                        val validateRequest = ValidateRequest(etEmail.text.toString(), responseListener )
                        val queue = Volley.newRequestQueue(this@Register2)
                        queue.add(validateRequest)

                        btnNext.setOnClickListener {
                            fun onClick() {

                                val intent = Intent(this@Register2, Register3::class.java)

                                intent.putExtra("birthday", birthday)
                                intent.putExtra("email", email.toString())
                                startActivity(intent)
                            }
                            onClick()
                        }

                    }
                    else {



                        checkSign.visibility = View.INVISIBLE
                        warningSign.visibility = View.INVISIBLE

                        btnNext.setOnClickListener{


                            Toast.makeText(this@Register2, "이메일주소를 확인해주세요", Toast.LENGTH_SHORT).show()


                        }
                    }

                }

                else{

                    checkSign.visibility = View.INVISIBLE
                    warningSign.visibility = View.INVISIBLE

                    btnNext.isEnabled = false

                    btnStyle()

                }



            }





        })





    }





}
