package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.post_search_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlacePage : AppCompatActivity() {

    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val postDataList = ArrayList<PostImageData>()
    val adapter = PostImageSearchAdapter(this, postDataList)
    var placeId  = 1
    var index = 0

    var isFollowing: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_page)

        val sharedPreference = SharedPreference()
        val email = sharedPreference.getString(this, "email")!!
        val backgroundColor = ContextCompat.getColor(this, R.color.background)
        val whiteColor = ContextCompat.getColor(this, R.color.white)

        val tvTopPageName = findViewById<TextView>(R.id.topPlaceName)
        val ivPlacePhoto = findViewById<ImageView>(R.id.placePhoto)
        val tvPostNumber = findViewById<TextView>(R.id.postNumber)
        val tvFollowerNumber = findViewById<TextView>(R.id.followerNumber)
        val tvLikeNumber = findViewById<TextView>(R.id.likeNumber)
        val tvPlaceName = findViewById<TextView>(R.id.placeName)
        val tvProvince = findViewById<TextView>(R.id.placeProvince)
        val tvType = findViewById<TextView>(R.id.placeType)
        val btnPost = findViewById<ImageView>(R.id.btnPost)
        val btnFollow = findViewById<Button>(R.id.btnFollow)
        val pageRecyclerView = findViewById<RecyclerView>(R.id.pageRecycler)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivLike = findViewById<ImageView>(R.id.ivLike)

        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)
        val mOnItemClickListener = object: OnItemClickListener{
            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

            }



        }

        val placeName = intent.getStringExtra("placeName")
        placeId = intent.getIntExtra("placeId", 1)
        val placePhoto = intent.getStringExtra("placePhoto")
        val placeType = intent.getStringExtra("placeType")
        val placeProvince = intent.getStringExtra("placeProvince")


        val checkFollowing: Call<ServerResonse> = apiInterface.checkFollowing(placeId, email)
        checkFollowing.enqueue(object : Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                if(response.isSuccessful){
                    isFollowing = response.body()?.isFollowing!!
                }
            }


        })
        if (isFollowing){
            btnFollow.apply {
                setBackgroundResource(R.drawable.button_shape)
                setTextColor(backgroundColor)
                text = "팔로잉"
            }
        } else{
            btnFollow.apply {
                setBackgroundResource(R.drawable.button_shape_green)
                setTextColor(whiteColor)
                text = "팔로우"
            }
        }





        btnPost.setOnClickListener {



        }

        btnFollow.setOnClickListener {

            val toFollow: Call<ServerResonse> = apiInterface.followPlace(placeId, email, isFollowing)
            toFollow.enqueue(object : Callback<ServerResonse>{
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ServerResonse>,
                    response: Response<ServerResonse>
                ) {
                   val success: Boolean = response.body()?.success!!
                    if(success){
                        isFollowing = response.body()?.isFollowing!!
                        if (isFollowing){
                            btnFollow.apply {
                                setBackgroundResource(R.drawable.button_shape)
                                setTextColor(backgroundColor)
                                text = "팔로잉"
                            }
                        } else{
                            btnFollow.apply {
                                setBackgroundResource(R.drawable.button_shape_green)
                                setTextColor(whiteColor)
                                text = "팔로우"
                            }
                        }
                    }
                }


            })

        }



        ivBack.setOnClickListener {

            finish()
        }




        tvTopPageName.text = placeName
        tvPlaceName.text = placeName
        tvProvince.text = placeProvince
        tvType.text = placeType
        Glide.with(this).load(placePhoto).thumbnail(0.3f)
            .into(ivPlacePhoto)
        val callGetPageInfo: Call<PlacePageData> = apiInterface.getPageInfo(placeId)
        callGetPageInfo.enqueue(object : Callback<PlacePageData>{
            override fun onFailure(call: Call<PlacePageData>, t: Throwable) {

            }

            override fun onResponse(call: Call<PlacePageData>, response: Response<PlacePageData>) {
                tvPostNumber.text = response.body()!!.postNumber.toString()
                tvFollowerNumber.text = response.body()!!.followerNumber.toString()
                tvLikeNumber.text = response.body()!!.likeNumber.toString()
            }


        })







        adapter.apply{
            onItemClickListener = mOnItemClickListener
            addLoadMoreListener(object : SearchRecyclerAdapter.OnLoadMoreListener{
                override fun onLoadMore() {

                    pageRecyclerView.post(object : Runnable{
                        override fun run() {
                            index = postImageDataList.size -1
                            getMorePostData(index)
                        }
                    })

                }

            })
            notifyDataChanged()


        }
        pageRecyclerView.apply{
            addItemDecoration(dividerDecoration)
            itemAnimator = DefaultItemAnimator()
            adapter = adapter
            layoutManager = GridLayoutManager(this@PlacePage, 3)

        }

        getPostData(index)





    }


    fun getPostData(index: Int){
        val callGetPagePosts: Call<ArrayList<PostImageData>> = apiInterface.getPagePosts(placeId, index)
        callGetPagePosts.enqueue(object : Callback<ArrayList<PostImageData>>{
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

    fun getMorePostData(index: Int){

        postDataList.add(PostImageData("load"))
        adapter.notifyItemInserted(postDataList.size -1 )

        val call: Call<ArrayList<PostImageData>> = apiInterface.getPagePosts(placeId, index)
        call.enqueue(object : Callback<ArrayList<PostImageData>>{
            override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PostImageData>>,
                response: Response<ArrayList<PostImageData>>
            ) {
                if (response.isSuccessful){
                    if(postDataList.size > 0 )
                        postDataList.removeAt(postDataList.size -1 )

                    val result: ArrayList<PostImageData>? = response.body()

                    if(!result.isNullOrEmpty() && result.size >0 ){
                        postDataList.addAll(result)

                    }else {

                        adapter.isMoreDataAvailable = false
                    }
                    adapter.notifyDataChanged()
                }
            }

        })

    }
}
