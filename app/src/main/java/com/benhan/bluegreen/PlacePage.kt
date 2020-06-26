package com.benhan.bluegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    var pageRecyclerView : RecyclerView? = null
    var welcome: TextView? = null

    var isFollowing: Boolean? = null
    var isLiking = false




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
        pageRecyclerView = findViewById<RecyclerView>(R.id.pageRecycler)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivLike = findViewById<ImageView>(R.id.ivLike)
        welcome = findViewById(R.id.welcome)





        val placeName = intent.getStringExtra("placeName")
        placeId = intent.getIntExtra("placeId", 1)
        val placePhoto = intent.getStringExtra("placePhoto")
        val placeType = intent.getStringExtra("placeType")
        val placeProvince = intent.getStringExtra("placeProvince")

        Log.d("아이디", placeId.toString())

        btnPost.setOnClickListener {
            val intent = Intent(this, PlusGalleryActivity::class.java)
            intent.putExtra("place_id", placeId)
            startActivity(intent)
        }

        val checkFollowing: Call<ServerResonse> = apiInterface.checkFollowing(placeId, email)
        checkFollowing.enqueue(object : Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                Log.d("팔로잉", t.message)

            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                    isFollowing = response.body()?.isFollowing!!

                    Log.d("팔로잉", isFollowing.toString())
                    if (isFollowing as Boolean) {
                        btnFollow.setBackgroundResource(R.drawable.button_shape_stroke)
                        btnFollow.setTextColor(backgroundColor)
                        btnFollow.text = "팔로잉"
                        return

                    } else{

                        btnFollow.setBackgroundResource(R.drawable.button_shape_green)
                        btnFollow.setTextColor(whiteColor)
                        btnFollow.text = "팔로우"

                        return

                    }

            }


        })
        val checkLiking: Call<ServerResonse> = apiInterface.checkLike(placeId, email)
        checkLiking.enqueue(object: Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                isLiking = response.body()?.isLiking!!
                if (isLiking){
                    ivLike.setImageResource(R.drawable.tree_selected)
                } else{
                    ivLike.setImageResource(R.drawable.tree)



                }
            }
        })









        ivBack.setOnClickListener {

            finish()
        }




        tvTopPageName.text = placeName
        tvPlaceName.text = placeName
        tvProvince.text = placeProvince
        tvType.text = placeType

        if(!placePhoto.isNullOrEmpty()) {
            Glide.with(this).load(placePhoto).thumbnail(0.3f)
                .into(ivPlacePhoto)
        }
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


        ivLike.setOnClickListener {
            if(!isLiking){
                ivLike.setImageResource(R.drawable.tree_selected)
                tvLikeNumber.text = (Integer.parseInt(tvLikeNumber.text.toString()) + 1).toString()

                val toLike: Call<ServerResonse> = apiInterface.likePlace(placeId, email, isLiking)
                toLike.enqueue(object : Callback<ServerResonse>{
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {
                        isLiking = true
                    }
                })


            } else {
                ivLike.setImageResource(R.drawable.tree)
                tvLikeNumber.text = (Integer.parseInt(tvLikeNumber.text.toString()) -1 ).toString()
                val toUnLike: Call<ServerResonse> = apiInterface.unlikePlace(placeId, email, isLiking)
                toUnLike.enqueue(object : Callback<ServerResonse>{
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {
                        isLiking = false
                    }
                })
            }


        }

        btnFollow.setOnClickListener {




            if(!isFollowing!!) {
                isFollowing = true
                btnFollow.setBackgroundResource(R.drawable.button_shape_stroke)
                btnFollow.setTextColor(backgroundColor)
                btnFollow.text = "팔로잉"
                tvFollowerNumber.text = ( Integer.parseInt(tvFollowerNumber.text.toString()) + 1 ).toString()
                val toFollow: Call<ServerResonse> =
                    apiInterface.followPlace(placeId, email, isFollowing!!)
                toFollow.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {
                        var success: Boolean = response.body()?.success!!
                        if (success) {


                        }
                    }


                })


            }else {
                isFollowing = false
                btnFollow.setBackgroundResource(R.drawable.button_shape_green)
                btnFollow.setTextColor(whiteColor)
                btnFollow.text = "팔로우"
                tvFollowerNumber.text = ( Integer.parseInt(tvFollowerNumber.text.toString()) - 1 ).toString()
                val toUnFollow: Call<ServerResonse> =
                    apiInterface.unfollowPlace(placeId, email, isFollowing!!)
                toUnFollow.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {
                        var success: Boolean = response.body()?.success!!
                        if (success) {


                        }
                    }


                })



            }
        }








        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)
        val mOnItemClickListener = object: OnItemClickListener{
            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {


                val name = sharedPreference.getString(this@PlacePage, "name")
                val post_id = postDataList[position].postId

                val intent = Intent(this@PlacePage, SearchFullPost::class.java)
                intent.putExtra("place_or_user_name", name)
                intent.putExtra("post_id", post_id)
                startActivity(intent)
            }



        }










        adapter.onItemClickListener = mOnItemClickListener





        pageRecyclerView?.addItemDecoration(dividerDecoration)
            pageRecyclerView?.itemAnimator = DefaultItemAnimator()
            pageRecyclerView?.adapter = adapter
            pageRecyclerView?.layoutManager = GridLayoutManager(this@PlacePage, 3)


        getPostData(placeId)

        val swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipeLayout)

        swipeLayout?.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {

                postDataList.removeAll(postDataList)
                adapter!!.notifyDataChanged()
                getPostData(placeId)
                swipeLayout.isRefreshing = false
            }


        })


    }




    fun getPostData(place_id: Int){
        val callGetPagePosts: Call<ArrayList<PostImageData>> = apiInterface.getPagePosts(place_id)
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


}
