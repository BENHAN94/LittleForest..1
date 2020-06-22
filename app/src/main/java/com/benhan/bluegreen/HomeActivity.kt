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
    val apiInterface: ApiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val update: Call<ServerResonse> = apiInterface.updatePlaceBestPost()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)




//        delete.enqueue(object : Callback<ServerResonse>{
//            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {
//            }
//
//        })
//
//        call.enqueue(object: Callback<ServerResonse>{
//            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
//
//                Log.d("업데이트", t.message)
//            }
//
//            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {
//
//                Log.d("업데이트", "성공")
//            }
//
//
//        })
//
//        update.enqueue(object: Callback<ServerResonse>{
//            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
//
//                Log.d("업데이트", t.message)
//            }
//
//            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {
//
//                Log.d("업데이트", "성공")
//            }
//
//
//        })
    }


    override fun onResume() {
        super.onResume()

        update.clone().enqueue(object: Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                Log.d("업데이트", t.message)
            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                Log.d("업데이트", "성공")
            }


        })

    }

    override fun onBackPressed() {

    }


}

























