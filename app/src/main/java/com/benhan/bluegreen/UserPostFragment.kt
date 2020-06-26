package com.benhan.bluegreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPostFragment: Fragment() {

    val postImageDataList = ArrayList<PostImageData>()
    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    var adapter: PostImageSearchAdapter? = null
    var myEmail: String? = null
    val sharedPreference = SharedPreference()
    var welcome: TextView? = null
    var recyclerView : RecyclerView? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.search_post_fragment, container, false)
        myEmail = sharedPreference.getString(requireContext(), "email")
        recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview)
        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)
        recyclerView!!.hasFixedSize()
        recyclerView!!.layoutManager = LinearLayoutManager(requireContext())
        recyclerView!!.addItemDecoration(dividerDecoration)
        welcome = rootView.findViewById(R.id.welcome)

        adapter = PostImageSearchAdapter(requireContext(), postImageDataList)
        recyclerView!!.adapter = adapter

        val mOnItemClickListener = object: OnItemClickListener{
            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val name = postImageDataList[position].name
                val post_id = postImageDataList[position].postId
                val intent = Intent(requireContext(), SearchFullPost::class.java)
                intent.putExtra("place_or_user_name", name)
                intent.putExtra("post_id", post_id)
                startActivity(intent)
            }
        }


        val onLoadMoreListener = object: HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {

                recyclerView!!.post(object : Runnable{
                    override fun run() {
                        val index = postImageDataList.size-1
                        loadMore(index)
                    }

                })
            }

        }
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter!!.onItemClickListener = mOnItemClickListener
        adapter!!.loadMoreListener = onLoadMoreListener


        if(adapter?.itemCount == 0)
        load(0)




        return rootView



    }





















    fun load(index: Int){

        val call: Call<ArrayList<PostImageData>> = apiInterface.getUserPostImage(myEmail!!, index)
        call.enqueue(object: Callback<ArrayList<PostImageData>> {
            override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PostImageData>>,
                response: Response<ArrayList<PostImageData>>
            ) {

                if(response.isSuccessful) {
                    response.body()?.let { postImageDataList.addAll(it) }
                    adapter?.notifyDataChanged()


                }
            }


        })


    }

    fun loadMore(index: Int){


        postImageDataList.add(PostImageData("load"))
        adapter?.notifyItemInserted(postImageDataList.size - 1)

        val newIndex = index + 1
        val call: Call<ArrayList<PostImageData>> = apiInterface.getUserPostImage(myEmail!!, newIndex)
        call.enqueue(object : Callback<ArrayList<PostImageData>> {
            override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PostImageData>>,
                response: Response<ArrayList<PostImageData>>
            ) {
                if(response.isSuccessful){
                    postImageDataList.removeAt(postImageDataList.size - 1)
                    val result: ArrayList<PostImageData>? = response.body()
                    if(result!!.size > 0) {
                        postImageDataList.addAll(result)
                    }else {
                        adapter!!.isMoreDataAvailable = false
                    }
                    adapter!!.notifyDataChanged()
                }

            }

        })




    }
}