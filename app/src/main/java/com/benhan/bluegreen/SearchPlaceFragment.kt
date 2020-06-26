package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.EventLog
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceFragment: Fragment() {


    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    var adapter: SearchRecyclerAdapter? = null
    val places = ArrayList<PlaceSearchData>()
    var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = layoutInflater.inflate(R.layout.search_place_fragment, container, false)
        val recyclerView = rootview.findViewById<RecyclerView>(R.id.recyclerview)
        adapter = SearchRecyclerAdapter(requireContext(), places)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()

        val placeOnItemClickListener = object: OnItemClickListener{

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

        val onLoadMoreListener = object : HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                recyclerView.post(object : Runnable{
                    override fun run() {
                        val index = places.size - 1
                        loadMore(keyword , index)
                    }

                })
            }
        }

        adapter?.onLoadMoreListener = onLoadMoreListener

        recyclerView.adapter = adapter









        val searchBar = rootview.findViewById<EditText>(R.id.searchBar)



        searchBar.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    keyword = searchBar.text.toString()


                    if (!keyword.isNullOrEmpty()){
                        places.removeAll(places)
                        recyclerView.removeAllViews()
                        load(keyword, 0)
                    }
                    hideKeyboard(requireActivity())
                    searchBar.text = null
                    searchBar.clearFocus()

                }
                return false
            }

        })



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




        load(keyword, 0)

        return rootview
    }













    fun load(keyword: String ,index: Int) {


        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.searchPlace(keyword, index)
        call.enqueue(object: Callback<ArrayList<PlaceSearchData>> {
            override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PlaceSearchData>>,
                response: Response<ArrayList<PlaceSearchData>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { places.addAll(it) }
                    adapter?.notifyDataChanged()
                }
            }


        })

    }





    fun loadMore(keyword: String, index: Int){


        places.add(PlaceSearchData("load"))
        adapter?.notifyItemInserted(places.size-1)

        val newIndex = index + 1
        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.searchPlace(keyword, newIndex)
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

}