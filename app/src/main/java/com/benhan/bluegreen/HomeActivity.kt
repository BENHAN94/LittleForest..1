package com.benhan.bluegreen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HomeActivity : AppCompatActivity() {






    private val TAG: String = "로그"



    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        var isProfileUpdated = intent.getBooleanExtra("profile_updated", false)



        val delete: Call<ServerResonse> = apiInterface.deletePostWithNullPlace()
        delete.enqueue(object : Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {
            }

        })



        val call: Call<ServerResonse> = apiInterface.updatePlaceBestPost()
        call.enqueue(object: Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                Log.d("업데이트", t.message)
            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                Log.d("업데이트", "성공")
            }


        })




        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionListener = object : PermissionListener{

            override fun onPermissionGranted() {
                startActivity(Intent(this@HomeActivity, PlusActivity::class.java))

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@HomeActivity, "권한 거부\n" + deniedPermissions.toString(),
                Toast.LENGTH_SHORT).show()
            }

        }


        val fragmentManager = supportFragmentManager

        val fragmentBell = FragmentBell()
        val fragmentSearch = FragmentSearch()
        val fragmentTree = FragmentTree()
        val fragmentUser = FragmentUser()


        val tree = findViewById<ImageView>(R.id.tree)
        val search = findViewById<ImageView>(R.id.search)
        val bell = findViewById<ImageView>(R.id.bell)
        val user = findViewById<ImageView>(R.id.user)
        val plus = findViewById<ImageView>(R.id.plus)




        plus.setOnClickListener {



            if(permissionCheck == PackageManager.PERMISSION_DENIED) {


                TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("사진첩을 열기 위해서는 갤러리 접근 권한이 필요해요")
                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }


            else if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onClicked()
            }
        }

        val transaction = fragmentManager.beginTransaction()




        transaction.replace(R.id.frameLayout, fragmentTree)
            .commitAllowingStateLoss()

        fun clickHandler(view: ImageView){



            view.setOnClickListener {

                val transaction = fragmentManager.beginTransaction()

            when(view.id) {

                R.id.tree -> {
                    transaction.replace(R.id.frameLayout, fragmentTree)




                    tree.setImageResource(R.drawable.tree_selected)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)

                }
                R.id.search -> {
                    transaction.replace(R.id.frameLayout, fragmentSearch)


                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search_selected)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)

                }
                R.id.bell -> {
                    transaction.replace(R.id.frameLayout, fragmentBell)



                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell_selected)
                    user.setImageResource(R.drawable.user)
                }
                R.id.user -> {
                    transaction.replace(R.id.frameLayout, fragmentUser)


                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user_selected)
                }


            }

                transaction.commit()




            }



        }


        clickHandler(tree)
        clickHandler(search)
        clickHandler(bell)
        clickHandler(user)


        if (isProfileUpdated) {
            user.performClick()
            isProfileUpdated=false
        }


    }

    private fun onClicked( ) {




        
        Log.d(TAG, "HomeActivity - onClicked() called")

        val intent = Intent(this, PlusGalleryActivity::class.java)

        startActivity(intent)
        finish()

    }

    override fun onBackPressed() {

    }


    override fun onStart() {
        super.onStart()
        val call: Call<ServerResonse> = apiInterface.updatePlaceBestPost()
        call.enqueue(object: Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                Log.d("업데이트", t.message)
            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                Log.d("업데이트", "성공")
            }


        })
    }

}

























