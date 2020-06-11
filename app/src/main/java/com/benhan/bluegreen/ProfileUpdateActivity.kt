package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest

class ProfileUpdateActivity : AppCompatActivity() {

    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)

        val etActName = findViewById<EditText>(R.id.etActname)
        val etName = findViewById<EditText>(R.id.etName)
        val etJob = findViewById<EditText>(R.id.etJob)
        val etIntroduce = findViewById<EditText>(R.id.etIntroduce)






        val sharedPreference = SharedPreference()
        var actualname: String?
        var name: String?
        var job: String?
        var introduction: String?
        var profile_photo: String?
        val email = sharedPreference.getString(this, "email")
        val tvChangeProfilePhoto = findViewById<TextView>(R.id.changeProfilePhoto)
        val userUpdateProfilePhoto = findViewById<ImageView>(R.id.userProfileUpdateProfilePhoto)
        val done = findViewById<TextView>(R.id.profileUpdateDone)

        val call: Call<User> = this.apiInterface.getUserProfile(email!!)
        call.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
             Log.d("프로필받기", t.message)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                actualname = response.body()?.actualname
                name = response.body()?.name
                job = response.body()?.job
                introduction = response.body()?.introduction
                profile_photo = response.body()?.profilephoto

                etActName.setText(actualname)
                etName.setText(name)
                etJob.setText(job)
                etIntroduce.setText(introduction)

                sharedPreference.setString(this@ProfileUpdateActivity,"actualName", actualname!!)
                sharedPreference.setString(this@ProfileUpdateActivity, "name", name!!)
                sharedPreference.setString(this@ProfileUpdateActivity, "job", job!!)
                sharedPreference.setString(this@ProfileUpdateActivity, "introduction", introduction!!)
                sharedPreference.setString(this@ProfileUpdateActivity, "profile_photo", profile_photo!!)

                Glide.with(this@ProfileUpdateActivity)
                    .load(profile_photo)
                    .into(userUpdateProfilePhoto)

            }


        })




        done.setOnClickListener {
            val textActName = etActName.text.toString()
            val textName = etName.text.toString()
            val textJob = etJob.text.toString()
            val textIntroduce = etIntroduce.text.toString()
            val updateProfile:Call<User> = this.apiInterface.updateProfile(textActName,
                textName,
                textJob,
                textIntroduce,
                email)


            updateProfile.enqueue(object : Callback<User>{
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("프로필업데이트", t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    finish()

                    sharedPreference.setString(this@ProfileUpdateActivity, "actual_name", etActName.text.toString() )
                    sharedPreference.setString(this@ProfileUpdateActivity, "introduction", etIntroduce.text.toString() )

                }


            })

            hideKeyboard(this)
        }
        val cancel = findViewById<TextView>(R.id.profileUpdateCancel)









        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                startActivity(Intent(this@ProfileUpdateActivity, ProfilePhotoPicker::class.java))


            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                Toast.makeText(this@ProfileUpdateActivity,"권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
            }


        }





        tvChangeProfilePhoto.setOnClickListener {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("프로필 사진을 바꾸기 위해서는 쓰기 권한이 필요해요")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()


            finish()

        }

        userUpdateProfilePhoto.setOnClickListener {

            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("프로필 사진을 바꾸기 위해서는 쓰기 권한이 필요해요")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()

            finish()

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
