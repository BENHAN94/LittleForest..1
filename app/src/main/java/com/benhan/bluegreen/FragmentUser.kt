package com.benhan.bluegreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentUser: Fragment() {


//    val prefConfig = PrefConfig(requireContext())


    val sharedPreference = SharedPreference()
    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val postDataList = ArrayList<PostImageData>()
    var ivProfilePhoto : ImageView? = null

    var likeNumber: Int?  =null
    var postNumber: Int? = null
    var followNumber: Int? = null
    var tvPostNumber :TextView? = null
    var tvFollowerNumber:TextView? = null
    var tvLikeNumber:TextView? = null
    var email: String? = null
    var adapter: PostImageSearchAdapter? = null
    var tvUsername: TextView? = null
    var tvActualname: TextView? = null
    var tvIntroduction: TextView? = null






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.home_fragment_user, container, false)


        val tree = rootView.findViewById<ImageView>(R.id.tree)
        val search = rootView.findViewById<ImageView>(R.id.search)
        val bell = rootView.findViewById<ImageView>(R.id.bell)
        val user = rootView.findViewById<ImageView>(R.id.user)


        tree.setImageResource(R.drawable.tree)
        search.setImageResource(R.drawable.search)
        bell.setImageResource(R.drawable.bell)
        user.setImageResource(R.drawable.user_selected)


        val permissionCheck = ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusGalleryActivity::class.java))

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    requireContext(), "권한 거부\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        val plus = rootView.findViewById<ImageView>(R.id.plus)

        plus.setOnClickListener {


            if (permissionCheck == PackageManager.PERMISSION_DENIED) {


                TedPermission.with(requireContext())
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("사진첩을 열기 위해서는 갤러리 접근 권한이 필요해요")
                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있어요")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            } else if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(requireActivity(), PlusGalleryActivity::class.java)
                startActivity(intent)
            }
        }

        val postFragment = UserPostFragment()
        val placeFragment = UserPlaceFragment()

        fun clickHandler(view: ImageView) {


            view.setOnClickListener {

                when (view.id) {
                    R.id.tree -> {
                        tree.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_tree)

                    }
                    R.id.search -> {
                        search.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_search)

                    }
                    R.id.bell -> {
                        bell.isClickable = false
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_bell)

                    }
                    R.id.user -> {
                        postFragment.recyclerView?.scrollToPosition(0)
                    }
                }
            }
        }


        clickHandler(tree)
        clickHandler(search)
        clickHandler(bell)


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        val ivGrid = rootView.findViewById<ImageView>(R.id.ivGrid)
        val ivLocation = rootView.findViewById<ImageView>(R.id.ivLocation)
        val gridTab = rootView.findViewById<RelativeLayout>(R.id.layoutGrid)
        val locationTab = rootView.findViewById<RelativeLayout>(R.id.layoutLocation)



        gridTab.isClickable = false
        locationTab.isClickable = true


        adapter = PostImageSearchAdapter(requireContext(), postDataList)
        val btnUpdate = rootView.findViewById<Button>(R.id.btnProfileUpdate)



        btnUpdate.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileUpdateActivity::class.java))
        }


        tvUsername = rootView.findViewById<TextView>(R.id.profile_username)
        tvActualname = rootView.findViewById<TextView>(R.id.actualName)
        tvIntroduction = rootView.findViewById<TextView>(R.id.userIntroduction)
        val tvLogout = rootView.findViewById<TextView>(R.id.logout)
        ivProfilePhoto = rootView.findViewById<ImageView>(R.id.placePhoto)
        email = sharedPreference.getString(requireContext(), "email")!!
        tvPostNumber = rootView.findViewById<TextView>(R.id.postNumber)
        tvFollowerNumber = rootView.findViewById<TextView>(R.id.followerNumber)
        tvLikeNumber = rootView.findViewById<TextView>(R.id.likeNumber)










        tvLogout.setOnClickListener {
            sharedPreference.clear(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity2::class.java))




        }





        /////////////////////////////////////////////////////////


        gridTab.setOnClickListener {

            gridTab.isClickable = false
            locationTab.isClickable = true

            gridTab.setBackgroundResource(R.drawable.button_shape_stroke)
            ivGrid.setImageResource(R.drawable.grid_green)

            locationTab.setBackgroundResource(R.drawable.button_shape_green)
            ivLocation.setImageResource(R.drawable.location_white)

            user.setOnClickListener {
                postFragment.recyclerView?.scrollToPosition(0)
            }

            replaceFragment(postFragment)


        }


        /////////////////////////////////////////////


        locationTab.setOnClickListener {

            gridTab.setBackgroundResource(R.drawable.button_shape_green)
            ivGrid.setImageResource(R.drawable.grid_white)

            locationTab.setBackgroundResource(R.drawable.button_shape_stroke)
            ivLocation.setImageResource(R.drawable.location_green)




            gridTab.isClickable = true
            locationTab.isClickable = false

            user.setOnClickListener {
                placeFragment.recyclerView?.scrollToPosition(0)
            }

            replaceFragment(placeFragment)


        }

        return rootView


    }


    override fun onResume() {
        super.onResume()

        val postFragment = UserPostFragment()
        tvUsername?.setText(sharedPreference.getString(requireContext(), "name"))
        tvActualname?.setText(sharedPreference.getString(requireContext(), "actualName"))
        tvIntroduction?.setText(sharedPreference.getString(requireContext(), "introduction"))

        update(email!!)
        replaceFragment(postFragment)
        val sharedPreference = SharedPreference()
        val profilePhotoUri = sharedPreference.getString(requireContext(), "profilePhoto")
        val profilePhoto = MyApplication.severUrl + profilePhotoUri
        Glide.with(requireActivity()).load(profilePhoto).thumbnail(0.3F)
            .into(ivProfilePhoto!!)





    }





    fun update(email: String){

        val call: Call<User> = apiInterface.update(email)
        call.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                likeNumber = response.body()?.likeNumber
                followNumber = response.body()?.followerNumber
                postNumber = response.body()?.postNumber
                tvFollowerNumber?.text = followNumber?.toString()
                tvLikeNumber?.text = likeNumber?.toString()
                tvPostNumber?.text = postNumber?.toString()
                sharedPreference.setInt(requireContext(), "postNumber", postNumber!!)
                sharedPreference.setInt(requireContext(), "followNumber", followNumber!!)

            }

        })

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

}