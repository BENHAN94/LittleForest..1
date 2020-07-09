package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.benhan.bluegreen.bell.FragmentBell
import com.benhan.bluegreen.network.ApiClient
import com.benhan.bluegreen.network.ApiInterface
import com.benhan.bluegreen.plus.PlusGalleryActivity
import com.benhan.bluegreen.search.FragmentSearch
import com.benhan.bluegreen.tree.FragmentTree
import com.benhan.bluegreen.user.FragmentUser
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


class HomeActivity : AppCompatActivity() {


    val apiClient = ApiClient()
    val apiInterface: ApiInterface = apiClient.getApiClient().create(
        ApiInterface::class.java)
//    val update: Call<ServerResonse> = apiInterface.updatePlaceBestPost()
    lateinit var tree: ImageView
    private lateinit var search :ImageView
    private lateinit var bell:ImageView
    lateinit var user:ImageView
    private lateinit var treeFragment : Fragment
    private lateinit var searchFragment : Fragment
    private lateinit var bellFragment: Fragment
    private lateinit var userFragment: Fragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        tree = findViewById(R.id.tree)
        search = findViewById(R.id.search)
        bell = findViewById(R.id.bell)
        user = findViewById(R.id.user)
        treeFragment = FragmentTree()
        searchFragment = FragmentSearch()
        bellFragment = FragmentBell()
        userFragment = FragmentUser()


        hideKeyboard(this)


        clickHandler(tree)
        clickHandler(search)
        clickHandler(user)
        clickHandler(bell)




        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(this@HomeActivity, PlusGalleryActivity::class.java))

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@HomeActivity, "권한 거부\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT).show()
            }

        }
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
                val intent = Intent(this, PlusGalleryActivity::class.java)
                startActivity(intent)
            }
        }

        replaceFragment(treeFragment)

    }




    override fun onBackPressed() {

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

    private fun replaceFragment(fragment: Fragment) {
        val backStateName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val fragmentPopped: Boolean = fragmentManager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }







    fun clickHandler(view: ImageView){
        view.setOnClickListener {

            when(view.id) {
                R.id.tree -> {
                    replaceFragment(treeFragment)
                    tree.setImageResource(R.drawable.tree_selected)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)

                }
                R.id.search -> {
                    replaceFragment(searchFragment)
                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search_selected)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)
                }
                R.id.bell -> {
                    replaceFragment(bellFragment)
                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell_selected)
                    user.setImageResource(R.drawable.user)
                }
                R.id.user -> {
                    replaceFragment(userFragment)
                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user_selected)
                }
            }
        }
    }

}

























