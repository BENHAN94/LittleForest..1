package com.benhan.bluegreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_user, container, false)


        val tree = rootView.findViewById<ImageView>(R.id.tree)
        val search = rootView.findViewById<ImageView>(R.id.search)
        val bell = rootView.findViewById<ImageView>(R.id.bell)
        val user = rootView.findViewById<ImageView>(R.id.user)

        tree.setImageResource(R.drawable.tree)
        search.setImageResource(R.drawable.search)
        bell.setImageResource(R.drawable.bell)
        user.setImageResource(R.drawable.user_selected)

        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusActivity::class.java))

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
        fun clickHandler(view: ImageView){



            view.setOnClickListener {

                when(view.id) {
                    R.id.tree -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_tree)
                    }
                    R.id.search -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_search)
                    }
                    R.id.bell -> {
                        Navigation.findNavController(rootView).navigate(R.id.from_user_to_bell)
                    }
                    R.id.user -> {

                    }
                }
            }
        }


        clickHandler(tree)
        clickHandler(search)
        clickHandler(bell)







////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////








        val adapter = PostImageSearchAdapter(requireContext(), postDataList)
        val btnUpdate = rootView.findViewById<Button>(R.id.btnProfileUpdate)



        btnUpdate.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileUpdateActivity::class.java))
        }



        val tvUsername = rootView.findViewById<TextView>(R.id.profile_username)
        val tvActualname = rootView.findViewById<TextView>(R.id.actualName)
        val tvIntroduction = rootView.findViewById<TextView>(R.id.userIntroduction)
        val ivMenu = rootView.findViewById<ImageView>(R.id.profile_menu)
        ivProfilePhoto = rootView.findViewById<ImageView>(R.id.placePhoto)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.userPostRecycler)
        val email = sharedPreference.getString(requireContext(), "email")!!
        val tvPostNumber = rootView.findViewById<TextView>(R.id.postNumber)
        val tvFollowerNumber = rootView.findViewById<TextView>(R.id.followerNumber)
        val tvLikeNumber = rootView.findViewById<TextView>(R.id.likeNumber)

        tvUsername.setText(sharedPreference.getString(requireContext(), "name"))
        tvActualname.setText(sharedPreference.getString(requireContext(), "actualName"))
        tvIntroduction.setText(sharedPreference.getString(requireContext(), "introduction"))
        val profilePhotoUri = sharedPreference.getString(requireContext(), "profilePhoto")


        val getUserPageInfo: Call<UserPageData> = apiInterface.getUserPageInfo(email)
        getUserPageInfo.enqueue(object : Callback<UserPageData>{
            override fun onFailure(call: Call<UserPageData>, t: Throwable) {

            }

            override fun onResponse(call: Call<UserPageData>, response: Response<UserPageData>) {
                tvPostNumber.text = response.body()!!.postNumber.toString()
                tvFollowerNumber.text = response.body()!!.followerNumber.toString()
                tvLikeNumber.text = response.body()!!.likeNumber.toString()
            }


        })



        ivMenu.setOnClickListener {
            sharedPreference.clear(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity2::class.java))





        }

        fun getPostData(){
            val callGetPagePosts: Call<ArrayList<PostImageData>> = apiInterface.getUserPostImage(email)
            callGetPagePosts.enqueue(object : Callback<ArrayList<PostImageData>> {
                override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<PostImageData>>,
                    response: Response<ArrayList<PostImageData>>
                ) {

                    response.body()?.let { postDataList.addAll(it) }
                    adapter.notifyDataChanged()

                }


            })
        }

        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)
        val mOnItemClickListener = object: OnItemClickListener{
            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

            }



        }







        adapter.onItemClickListener = mOnItemClickListener





        recyclerView.addItemDecoration(dividerDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        if(adapter.itemCount == 0)
        getPostData()






        return rootView


    }


    override fun onResume() {
        super.onResume()

        val sharedPreference = SharedPreference()
        val profilePhotoUri = sharedPreference.getString(requireContext(), "profilePhoto")
        Glide.with(requireActivity()).load(profilePhotoUri).thumbnail(0.3F)
            .into(ivProfilePhoto!!)
    }




}