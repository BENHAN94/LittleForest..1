package com.benhan.bluegreen

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.create

class FragmentTree: Fragment() {


    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_tree, container, false)

        val sharedPreference = SharedPreference()
        val email = sharedPreference.getString(requireContext(), "email")!!

        val adapter = HomeRecyclerAdapter(requireContext())
        val recyclerview = rootView.findViewById<RecyclerView>(R.id.treeRecyclerView)
        recyclerview.adapter = adapter


        fun load() {

            apiInterface.getPostData(email, 0)
            adapter.notifyDataChanged()

        }

        load()



        return rootView


    }


}