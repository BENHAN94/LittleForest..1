package com.benhan.bluegreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.plus_fragment_gallery_upload.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherUser : AppCompatActivity() {


    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)

    var actual_name: String? = null
    var introduction: String? = null
    var postNumber: String? = null
    var followingNumber: String? = null
    var likeNumber: String? =null
    var profilePhoto: String? = null
    var email: String? = null
    var name: String? = null
    var postDataList = ArrayList<PostData>()
    var adapter :OtherUserPostAdapter? = null
    var viewModel : LFVIewModel? = null
    var dividerDecoration :GridDividerDecoration? = null
    var swipeLayout: SwipeRefreshLayout?  =null
    var index: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)


        val tvUsername = findViewById<TextView>(R.id.profile_username)
        val tvActualname = findViewById<TextView>(R.id.actualName)
        val tvIntroduction = findViewById<TextView>(R.id.userIntroduction)
        val ivMenu = findViewById<ImageView>(R.id.profile_menu)
        val ivProfilePhoto = findViewById<ImageView>(R.id.placePhoto)
        val recyclerView = findViewById<RecyclerView>(R.id.userPostRecycler)
        val tvPostNumber = findViewById<TextView>(R.id.postNumber)
        val tvFollowNumber = findViewById<TextView>(R.id.followNumber)
        val tvLikeNumber = findViewById<TextView>(R.id.likeNumber)
        name = intent.getStringExtra("otherUserName")

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        viewModel = ViewModelProvider(this).get(LFVIewModel::class.java)

        postDataList = viewModel!!.postDataList



        if(adapter == null)
        adapter = OtherUserPostAdapter(this, postDataList)
        adapter!!.addLoadMoreListener(object : OtherUserPostAdapter.OnLoadMoreListener{

            override fun onLoadMore() {

                recyclerView.post{

                    index = postDataList.size - 1
                    loadMore(index)

                }
            }

        })




        ///////////////////---------=====recyclerview======-----/////////////////////////////////////////////////////////////////////////////////

        dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.removeItemDecoration(dividerDecoration!!)
        recyclerView.addItemDecoration(dividerDecoration!!)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter



//////////////////////////////////////////////////////////////////////////////////////////////

        val call: Call<User> = apiInterface.getOtherUserData(name!!)
        call.enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {

                Log.d("아더유저", t.message)


            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                Log.d("아더유저", response.message())

                val res = response.body()!!

            actual_name = res.actualname
                introduction = res.introduction
                postNumber = res.postNumber.toString()
                followingNumber = res.followerNumber.toString()
                likeNumber = res.likeNumber.toString()
                profilePhoto = res.profilephoto
                email = res.email
                tvUsername.text = name
                tvActualname.text = actual_name
                tvIntroduction.text = introduction
                tvPostNumber.text = postNumber
                tvFollowNumber.text = followingNumber
                tvLikeNumber.text = likeNumber
                Glide.with(this@OtherUser)
                    .load(profilePhoto)
                    .into(ivProfilePhoto)


                if(postDataList.size == 0 && email !=null)
                    load()
            }

        })


///////////////////////////////////////////////////////////////////////////////////////////







///////////////////////////////////////////////////////////////////////////////////////////




//////////////////////////////////////////////////////////////////




    }

    fun load() {


        val call:Call<ArrayList<PostData>> = apiInterface.getOtherUserPost(email!!, 0)
        call.enqueue(object : Callback<ArrayList<PostData>>{
            override fun onFailure(call: Call<ArrayList<PostData>>, t: Throwable) {

                Log.d("콜에러", t.message)
            }

            override fun onResponse(
                call: Call<ArrayList<PostData>>,
                response: Response<ArrayList<PostData>>
            ) {
                Log.d("콜에러", response.message())

                response.body()?.let { postDataList.addAll(it) }
                adapter!!.notifyDataChanged()
            }

        })


    }

    fun loadMore(index: Int){


        postDataList.add(PostData("load"))
        adapter!!.notifyItemInserted(postDataList.size - 1)

        val call: Call<ArrayList<PostData>> = apiInterface.getOtherUserPost(email!!, index)
        call.enqueue(object : Callback<ArrayList<PostData>>{
            override fun onFailure(call: Call<ArrayList<PostData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PostData>>,
                response: Response<ArrayList<PostData>>
            ) {

                if(response.isSuccessful) {
                    if(postDataList.size > 0 )
                        postDataList.removeAt(postDataList.size - 1)

                    val result: ArrayList<PostData>? = response.body()


                    if(!result.isNullOrEmpty() && result.size > 0){
                        postDataList.addAll(result)
                    }else {

                        adapter!!.isMoreDataAvailable = false

                    }
                    adapter!!.notifyDataChanged()

                }
            }


        })




    }
}
