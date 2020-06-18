package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentSearch: Fragment(){


    val postImageDataList = ArrayList<PostImageData>()
    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val navi =  requireActivity().findViewById<LinearLayout>(R.id.navigation_bar)
        KeyboardVisibilityEvent.setEventListener(
            activity,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {

                    val handler = Handler()

                    handler.postDelayed(object : Runnable{
                        override fun run() {

                            if(!isOpen)
                                navi.visibility = View.VISIBLE


                        }


                    }, 10)

                    if(isOpen)
                    { navi.visibility = View.GONE}

                }
            })

        val places = ArrayList<PlaceSearchData>()
        val adapter = SearchRecyclerAdapter(requireContext(), places)
        val rootView = inflater.inflate(R.layout.home_fragment_search, container, false)
        val postImageAdapter = PostImageSearchAdapter(requireContext(), postImageDataList)
        val gridTab = rootView.findViewById<RelativeLayout>(R.id.layoutGrid)
        val locationTab = rootView.findViewById<RelativeLayout>(R.id.layoutLocation)
        val ivGrid = rootView.findViewById<ImageView>(R.id.ivGrid)
        val ivLocation = rootView.findViewById<ImageView>(R.id.ivLocation)
        val searchRecyclerView = rootView.findViewById<RecyclerView>(R.id.searchRecycler)
        val swipeLayout = rootView.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        val sharedPreference = SharedPreference()
        val email = sharedPreference.getString(requireContext(), "email")!!
        var index = 0
        val searchBar = rootView.findViewById<EditText>(R.id.search)


        searchBar.visibility = View.GONE
        searchBar.isFocusable = false



        postImageDataList.removeAll(postImageDataList)
        searchRecyclerView.removeAllViews()
        postImageAdapter.notifyDataChanged()

        fun getPostData(index: Int){

            val call:Call<ArrayList<PostImageData>> = apiInterface.getRandomPostImage(index)
            call.enqueue(object: Callback<ArrayList<PostImageData>>{
                override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<PostImageData>>,
                    response: Response<ArrayList<PostImageData>>
                ) {

                    response.body()?.let { postImageDataList.addAll(it) }
                    postImageAdapter.notifyDataChanged()

                }


            })


        }

        fun getMorePostData(index: Int){


            postImageDataList.add(PostImageData("load"))
            postImageAdapter.notifyItemInserted(postImageDataList.size - 1)

            val call: Call<ArrayList<PostImageData>> = apiInterface.getRandomPostImage(index)
            call.enqueue(object : Callback<ArrayList<PostImageData>>{
                override fun onFailure(call: Call<ArrayList<PostImageData>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<PostImageData>>,
                    response: Response<ArrayList<PostImageData>>
                ) {

                    if(response.isSuccessful) {
                        if(postImageDataList.size > 0 )
                        postImageDataList.removeAt(postImageDataList.size - 1)

                        val result: ArrayList<PostImageData>? = response.body()


                            if(!result.isNullOrEmpty() && result.size > 0){
                                postImageDataList.addAll(result)
                            }else {

                                postImageAdapter.isMoreDataAvailable = false

                            }
                        postImageAdapter.notifyDataChanged()

                    }
                }


            })




        }

        fun load(keyword: String ,index: Int){





            val call:Call<ArrayList<PlaceSearchData>> = apiInterface.loadPlace(keyword, index)

            call.enqueue(object : Callback<ArrayList<PlaceSearchData>>{
                override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

                    Log.d("실패", t.message)
                }

                override fun onResponse(
                    call: Call<ArrayList<PlaceSearchData>>,
                    response: Response<ArrayList<PlaceSearchData>>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let { places.addAll(it) }
                        adapter.notifyDataChanged()
                        Log.d("플레이스", places.isEmpty().toString())
                    } else{ Log.d("메세지", response.isSuccessful.toString())}
                }


            })




        }

        fun loadMore(keyword: String, startRow: Int){


            places.add(PlaceSearchData("load"))
            adapter.notifyItemInserted(places.size-1)


            val call:Call<ArrayList<PlaceSearchData>> = apiInterface.searchPlace(keyword, startRow)
            call.enqueue(object : Callback<ArrayList<PlaceSearchData>>{
                override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<PlaceSearchData>>,
                    response: Response<ArrayList<PlaceSearchData>>
                ) {

                    if(response.isSuccessful){
                        if(places.size > 0)
                            places.removeAt(places.size - 1)
                        val result: ArrayList<PlaceSearchData>? = response.body()

                        if(!result.isNullOrEmpty() && result.size > 0) {

                            places.addAll(result)
                        }else {
                                adapter.isMoreDataAvailable = false

                            }

                            adapter.notifyDataChanged()



                    }else {

                        Log.e("콜", response.message())

                    }

                }


            })




        }

        searchRecyclerView.removeAllViews()
        searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)

        val mOnItemClickListener = object: OnItemClickListener{
            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {


                val intent = Intent(requireContext(), SearchFullPost::class.java)
                startActivity(intent)

            }



        }

        swipeLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                postImageDataList.removeAll(postImageDataList)
                searchRecyclerView.removeAllViews()
                postImageAdapter.notifyDataChanged()
                getPostData(0)
                swipeLayout.isRefreshing = false
            }


        })

        postImageAdapter.onItemClickListener = mOnItemClickListener
        searchRecyclerView.addItemDecoration(dividerDecoration)
        searchRecyclerView.itemAnimator = DefaultItemAnimator()
        postImageAdapter.addLoadMoreListener(object: SearchRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {

                searchRecyclerView.post(object: Runnable{
                    override fun run() {
                        index = postImageDataList.size - 1
                        getMorePostData(index)
                    }
                })
            }


        })


        searchRecyclerView.adapter = postImageAdapter

        postImageAdapter.notifyDataChanged()

        getPostData(0)


        gridTab.setOnClickListener {

            hideKeyboard(requireActivity())
            searchBar.visibility = View.GONE

            searchBar.isFocusable = false

            gridTab.setBackgroundResource(R.drawable.button_shape_green)
            ivGrid.setImageResource(R.drawable.grid_white)

            locationTab.setBackgroundResource(R.drawable.button_shape)
            ivLocation.setImageResource(R.drawable.location_green)
            searchRecyclerView.hasFixedSize()
            searchRecyclerView.removeAllViews()
            searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

            val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)

            val mOnItemClickListener = object: OnItemClickListener{
                override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

                    val intent = Intent(requireContext(), SearchFullPost::class.java)


                    startActivity(intent)

                }



            }

            swipeLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
                override fun onRefresh() {
                    postImageDataList.removeAll(postImageDataList)
                    searchRecyclerView.removeAllViews()
                    postImageAdapter.notifyDataChanged()
                    getPostData(0)
                    swipeLayout.isRefreshing = false
                }


            })

            postImageAdapter.onItemClickListener = mOnItemClickListener
            searchRecyclerView.addItemDecoration(dividerDecoration)
            searchRecyclerView.itemAnimator = DefaultItemAnimator()
            postImageAdapter.addLoadMoreListener(object: SearchRecyclerAdapter.OnLoadMoreListener{
                override fun onLoadMore() {

                    searchRecyclerView.post(object: Runnable{
                        override fun run() {
                            index = postImageDataList.size - 1
                            getMorePostData(index)
                        }
                    })
                }


            })


            searchRecyclerView.adapter = postImageAdapter

            postImageAdapter.notifyDataChanged()

            getPostData(0)






        }

        locationTab.setOnClickListener {

            var id: Int? = null
            var keyword: String = ""

            searchBar.visibility = View.VISIBLE

            gridTab.setBackgroundResource(R.drawable.button_shape)
            ivGrid.setImageResource(R.drawable.grid_green)

            locationTab.setBackgroundResource(R.drawable.button_shape_green)
            ivLocation.setImageResource(R.drawable.location_white)


            postImageDataList.removeAll(postImageDataList)
            searchRecyclerView.removeAllViews()
            adapter.notifyDataChanged()

            searchRecyclerView.adapter = adapter
            searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())



            val mOnItemClickListener = object: OnItemClickListener{

                override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

                    val intent = Intent(requireContext(), PlacePage::class.java)
                    val selectedPlace = places[position]
                    val placeName = selectedPlace.name
                    val placeId =selectedPlace.id
                    val placePhoto = selectedPlace.photo
                    val placeType = selectedPlace.type
                    val placeProvince = selectedPlace.province
                    intent.putExtra("placeName", placeName)
                    intent.putExtra("placeId", placeId)
                    intent.putExtra("placePhoto", placePhoto)
                    intent.putExtra("placeType", placeType)
                    intent.putExtra("placeProvince", placeProvince)
                    startActivity(intent)



                }

            }

            adapter.onItemClickListener = mOnItemClickListener

            searchBar.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {







                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    keyword = searchBar.text.toString()
                    if (keyword.isNullOrEmpty()){
                        keyword = ""}
                    places.removeAll(places)
                    searchRecyclerView.removeAllViews()
                    adapter.notifyDataChanged()
                    load(keyword, 0)


                }


            })
            searchBar.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchBar.clearFocus()
                        hideKeyboard(requireActivity())
                        searchBar.isFocusable = false
                    }
                    return false
                }


            })



            searchBar.setOnTouchListener(object: View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    searchBar.isFocusableInTouchMode = true
                    return false
                }


            })




            load(keyword, index)


            adapter.addLoadMoreListener(object: SearchRecyclerAdapter.OnLoadMoreListener{
                override fun onLoadMore() {

                    searchRecyclerView.post(object : Runnable{
                        override fun run() {

                            index = places.size - 1

                            loadMore(keyword , index)

                        }

                    })

                }


            })


            swipeLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
                override fun onRefresh() {
                    places.removeAll(places)
                    searchRecyclerView.removeAllViews()
                    adapter.notifyDataChanged()
                    load(keyword, 0)
                    swipeLayout.isRefreshing = false
                }


            })



            searchBar.setOnEditorActionListener(object : TextView.OnEditorActionListener
            {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE){
                        keyword = searchBar.text.toString()
                        if (keyword.isNullOrEmpty()){
                            keyword = ""}
                        places.removeAll(places)
                        searchRecyclerView.removeAllViews()
                        adapter.notifyDataChanged()
                        load(keyword, 0)

                        searchBar.isFocusable = false
                        hideKeyboard(requireActivity())
                    }
                    return false
                }

            })
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

    override fun onStop() {
        super.onStop()
    }




}