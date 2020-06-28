package com.benhan.bluegreen

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.*
import java.util.jar.Manifest


class FragmentSearch: Fragment(){






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.home_fragment_search, container, false)

        val tree = rootView.findViewById<ImageView>(R.id.tree)
        val search = rootView.findViewById<ImageView>(R.id.search)
        val bell = rootView.findViewById<ImageView>(R.id.bell)
        val user = rootView.findViewById<ImageView>(R.id.user)

        tree.setImageResource(R.drawable.tree)
        search.setImageResource(R.drawable.search_selected)
        bell.setImageResource(R.drawable.bell)
        user.setImageResource(R.drawable.user)


        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusGalleryActivity::class.java))

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한 거부\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT).show()
            }

        }
        val plus = rootView.findViewById<ImageView>(R.id.plus)
        plus.setOnClickListener {



            if(permissionCheck == PackageManager.PERMISSION_DENIED) {


                TedPermission.with(requireContext())
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("사진첩을 열기 위해서는 갤러리 접근 권한이 필요해요")
                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }


            else if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(requireActivity(), PlusGalleryActivity::class.java)
                startActivity(intent)
            }
        }




        val postFragment = SearchPostFragment()
        val placeFragment = SearchPlaceFragment()
        fun clickHandler(view: ImageView){



            view.setOnClickListener {

                when(view.id) {
                    R.id.tree -> {
                        tree.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_search_to_tree)
                    }
                    R.id.search -> {
                        postFragment.recyclerView?.scrollToPosition(0)
                    }
                    R.id.bell -> {
                        bell.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_search_to_bell)
                    }
                    R.id.user -> {
                        user.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_search_to_user)
                    }
                }
            }
        }


        clickHandler(tree)
        clickHandler(bell)
        clickHandler(user)










        val gridTab = rootView.findViewById<RelativeLayout>(R.id.layoutGrid)
        val locationTab = rootView.findViewById<RelativeLayout>(R.id.layoutLocation)
        val ivGrid = rootView.findViewById<ImageView>(R.id.ivGrid)
        val ivLocation = rootView.findViewById<ImageView>(R.id.ivLocation)

        replaceFragment(postFragment)



        gridTab.setOnClickListener {


            gridTab.isClickable = false
            locationTab.isClickable = true
            hideKeyboard(requireActivity())

            gridTab.setBackgroundResource(R.drawable.button_shape_stroke)
            ivGrid.setImageResource(R.drawable.grid_green)

            locationTab.setBackgroundResource(R.drawable.button_shape_green)
            ivLocation.setImageResource(R.drawable.location_white)

            search.setOnClickListener {
                postFragment.recyclerView?.scrollToPosition(0)
            }

            replaceFragment(postFragment)

        }


        locationTab.setOnClickListener {


            gridTab.isClickable = true
            locationTab.isClickable = false

            gridTab.setBackgroundResource(R.drawable.button_shape_green)
            ivGrid.setImageResource(R.drawable.grid_white)

            locationTab.setBackgroundResource(R.drawable.button_shape_stroke)
            ivLocation.setImageResource(R.drawable.location_green)

            search.setOnClickListener {
                placeFragment.recyclerView?.scrollToPosition(0)
            }


            replaceFragment(placeFragment)
        }





        return rootView
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
        val childFragmentManager = childFragmentManager
        val fragmentPopped: Boolean = childFragmentManager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft = childFragmentManager.beginTransaction()
            ft.replace(R.id.frame, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    fun getCurrentAddress(
        latitude: Double,
        longitude: Double
    ): String? { /*지오코더... GPS를 주소로 변환*/
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        addresses =  geocoder.getFromLocation(latitude, longitude, 100)
        val address: Address = addresses[0]
        return address.getAddressLine(0).toString().toString() + "\n"
    }






}