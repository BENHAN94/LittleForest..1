package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class ProfileUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)


        val cancel = findViewById<TextView>(R.id.profileUpdateCancel)
        val etActName = findViewById<EditText>(R.id.etActname)
        val etName = findViewById<EditText>(R.id.etName)
        val etJob = findViewById<EditText>(R.id.etJob)
        val etIntroduce = findViewById<EditText>(R.id.etIntroduce)
        val tvChangeProfilePhoto = findViewById<TextView>(R.id.changeProfilePhoto)
        val userUpdateProfilePhoto = findViewById<ImageView>(R.id.userProfileUpdateProfilePhoto)

        tvChangeProfilePhoto.setOnClickListener {
            startActivity(Intent(this, ProfilePhotoPicker::class.java))
        }

        userUpdateProfilePhoto.setOnClickListener {
            startActivity(Intent(this, ProfilePhotoPicker::class.java))
        }




        cancel.setOnClickListener {

            this.finish()
            hideKeyboard(this)

        }

        etActName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etActName.clearFocus()
                    hideKeyboard(this@ProfileUpdateActivity)
                }
                return false
            }
        })

        etName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etActName.clearFocus()
                    hideKeyboard(this@ProfileUpdateActivity)
                }
                return false
            }
        })

        etJob.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etActName.clearFocus()
                    hideKeyboard(this@ProfileUpdateActivity)
                }
                return false
            }
        })

        etIntroduce.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etActName.clearFocus()
                    hideKeyboard(this@ProfileUpdateActivity)
                }
                return false
            }
        })
    }


    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
