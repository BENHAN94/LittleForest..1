package com.benhan.bluegreen.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.benhan.bluegreen.place.PlacePage
import com.benhan.bluegreen.R
import com.benhan.bluegreen.adapter.HomeRecyclerAdapter
import com.benhan.bluegreen.adapter.SearchRecyclerAdapter
import com.benhan.bluegreen.dataclass.PlaceSearchData
import com.benhan.bluegreen.listener.OnItemClickListener
import com.benhan.bluegreen.localdata.SharedPreference
import com.benhan.bluegreen.network.ApiClient
import com.benhan.bluegreen.network.ApiInterface
import com.pnikosis.materialishprogress.ProgressWheel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPlaceFragment: Fragment() {



    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    var adapter: SearchRecyclerAdapter? = null
    val places = ArrayList<PlaceSearchData>()
    var myEmail: String? = null
    val sharedPreference = SharedPreference()
    var recyclerView : RecyclerView? = null
    var index = 0
    var tvWhenEmptyFollow :TextView? = null
    var progressWheel: ProgressWheel? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = layoutInflater.inflate(R.layout.search_place_fragment, container, false)

        recyclerView = rootview.findViewById(R.id.recyclerview)
        swipeRefreshLayout = rootview.findViewById(R.id.swipeLayout)
        adapter = SearchRecyclerAdapter(
            requireContext(),
            places
        )



        myEmail = sharedPreference.getString(requireContext(), "email")
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.hasFixedSize()
        tvWhenEmptyFollow = rootview.findViewById(R.id.tvWhenEmptyFollow)
        val searchBar: EditText = rootview.findViewById(R.id.searchBar)
        searchBar.visibility = View.GONE
        progressWheel = rootview.findViewById(R.id.progress_wheel)

        val placeOnItemClickListener = object:
            OnItemClickListener {

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
        adapter?.onItemClickListener = placeOnItemClickListener






        recyclerView?.adapter = adapter









        val backgroundColor = ContextCompat.getColor(requireContext(),
            R.color.background
        )
        swipeRefreshLayout?.setColorSchemeColors(backgroundColor)
        swipeRefreshLayout?.setOnRefreshListener {

            places.removeAll(places)
            adapter?.notifyDataChanged()
            if(places.size == 0)
            load(0)
            adapter?.isMoreDataAvailable = true

            swipeRefreshLayout?.isRefreshing = false

        }




        if(adapter?.itemCount == 0) {
            progressWheel?.visibility = View.VISIBLE
            swipeRefreshLayout?.visibility = View.GONE
            load(0)
        }

        return rootview
    }













    fun load(index: Int) {


        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.getOtherUserPlace(myEmail!!, index)
        call.enqueue(object: Callback<ArrayList<PlaceSearchData>> {
            override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PlaceSearchData>>,
                response: Response<ArrayList<PlaceSearchData>>
            ) {
                progressWheel?.visibility = View.GONE
                swipeRefreshLayout?.visibility = View.VISIBLE

                if(response.isSuccessful){
                    response.body()?.let { places.addAll(it) }
                    adapter?.notifyDataChanged()

                    if(response.body()?.size == 20 && index == 0){
                        setOnLoadMoreListener()
                    }

                    if(places.size ==0){
                        tvWhenEmptyFollow?.visibility = View.VISIBLE
                    }

                }
            }


        })

    }





    fun loadMore(index: Int){


        places.add(PlaceSearchData("load"))
        adapter?.notifyItemInserted(places.size-1)

        val newIndex = index + 1
        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.getOtherUserPlace(myEmail!!, newIndex)
        call.enqueue(object : Callback<ArrayList<PlaceSearchData>> {
            override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PlaceSearchData>>,
                response: Response<ArrayList<PlaceSearchData>>
            ) {
                if(response.isSuccessful){
                    places.removeAt(places.size - 1)
                    val result: ArrayList<PlaceSearchData>? = response.body()
                    if(result!!.size > 0) {
                        places.addAll(result)
                    }else {
                        adapter!!.isMoreDataAvailable = false
                    }
                    adapter!!.notifyDataChanged()
                }
            }


        })




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

    fun setOnLoadMoreListener(){
        val onLoadMoreListener = object : HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                recyclerView?.post(object : Runnable{
                    override fun run() {
                        index = places.size - 1
                        loadMore(index)
                    }

                })
            }
        }

        adapter?.onLoadMoreListener = onLoadMoreListener

    }

}