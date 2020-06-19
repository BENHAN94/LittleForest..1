package com.benhan.bluegreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentUser: Fragment() {


//    val prefConfig = PrefConfig(requireContext())


    val sharedPreference = SharedPreference()
    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val postDataList = ArrayList<PostImageData>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_user, container, false)
        val adapter = PostImageSearchAdapter(requireContext(), postDataList)
        val btnUpdate = rootView.findViewById<Button>(R.id.btnProfileUpdate)

        btnUpdate.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileUpdateActivity::class.java))
        }

        val tvUsername = rootView.findViewById<TextView>(R.id.profile_username)
        val tvActualname = rootView.findViewById<TextView>(R.id.actualName)
        val tvIntroduction = rootView.findViewById<TextView>(R.id.userIntroduction)
        val ivMenu = rootView.findViewById<ImageView>(R.id.profile_menu)
        val ivProfilePhoto = rootView.findViewById<ImageView>(R.id.placePhoto)
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

        Glide.with(requireActivity()).load(profilePhotoUri).thumbnail(0.3F)
            .into(ivProfilePhoto)

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








}