package com.benhan.bluegreen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ethanhua.skeleton.Skeleton
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentTree: Fragment(){


    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    var postDataList = ArrayList<PostData>()
    val sharedPreference = SharedPreference()
    var adapter: HomeRecyclerAdapter? =null
     var rootView: View? = null

    var recyclerview: RecyclerView? = null
    var commentContainer: LinearLayout? = null

    var imm: InputMethodManager? = null



    var tree: ImageView? =null
    var search : ImageView? =null
    var bell :ImageView?  =null
    var user: ImageView? = null
    var navi: LinearLayout? = null

    var welcome: TextView? = null
    var welcome2: TextView? = null
    var welcome3: TextView? = null








    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView == null)
         rootView =  inflater.inflate(R.layout.home_fragment_tree, container, false)





/////////////////////////////






        val profilePhoto = sharedPreference.getString(requireContext(), "profilePhoto")





        recyclerview = rootView?.findViewById<RecyclerView>(R.id.treeRecyclerView)
        adapter = HomeRecyclerAdapter(requireContext(), requireActivity(), postDataList, profilePhoto!!)

        adapter!!.addLoadMoreListener(object: HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                recyclerview!!.post(object : Runnable{
                    override fun run() {
                         val index = postDataList.size-1
                        loadMore(index)
                    }

                })
            }

        })
        recyclerview!!.layoutManager = SpeedyLinearLayoutManager(requireContext())
        recyclerview!!.adapter = adapter



        val swipeLayout = rootView?.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)

        swipeLayout?.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {

                postDataList.removeAll(postDataList)
                adapter!!.notifyDataChanged()
                load(0)
                swipeLayout.isRefreshing = false
            }


        })









        adapter!!.addWriteCommentClickListener(object :
                HomeRecyclerAdapter.OnWriteCommentClicked {
                override fun onWriteCommentClicked() {
                    navi?.visibility = View.GONE

                    Log.d("나비", "곤")
                }
            })
            adapter?.addOnPostClickListener(object : HomeRecyclerAdapter.OnPostClicked {
                override fun onPostClicked(position: Int) {
                    recyclerview?.smoothScrollToPosition(position)
                    Log.d("나비", "비져블")
                    val handler = Handler()
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            navi?.visibility = View.VISIBLE
                        }
                    }, 350)
                }
            })











//////////////////////////////////////////////////////////////////////




        welcome = rootView?.findViewById<TextView>(R.id.welcome)
        welcome2 = rootView?.findViewById<TextView>(R.id.welcome2)
        welcome3 = rootView?.findViewById<TextView>(R.id.welcome3)


        val skeletonScreen = Skeleton.bind(recyclerview)
            .adapter(adapter).
            shimmer(true).
            angle(20).
            duration(1000).
            load(R.layout.layout_default_item_skeleton).
            show()



        recyclerview!!.postDelayed(Runnable { skeletonScreen.hide() }, 1000)








        /////////////////////////////////////////////////////////

         tree = rootView!!.findViewById(R.id.tree)
         search = rootView!!.findViewById(R.id.search)
         bell = rootView!!.findViewById(R.id.bell)
         user = rootView!!.findViewById(R.id.user)
        navi = rootView!!.findViewById(R.id.navigation_bar)
        commentContainer = rootView!!.findViewById(R.id.writeCommentContainer)
        tree!!.setImageResource(R.drawable.tree_selected)
        search!!.setImageResource(R.drawable.search)
        bell!!.setImageResource(R.drawable.bell)
        user!!.setImageResource(R.drawable.user)

        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusGalleryActivity::class.java))
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한 거부\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT).show()
            }
        }
        val plus = rootView!!.findViewById<ImageView>(R.id.plus)
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
                        recyclerview!!.smoothScrollToPosition(0)
                    }
                    R.id.search -> {
                        Navigation.findNavController(rootView!!).saveState()
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_search)
                    }
                    R.id.bell -> {
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_bell)
                    }
                    R.id.user -> {
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_user)
                    }
                }


            }
        }



        clickHandler(tree!!)
        clickHandler(search!!)
        clickHandler(bell!!)
        clickHandler(user!!)






        if(adapter?.itemCount == 0)
        load(0)

        return rootView



    }


    fun load(index: Int) {

        val email = sharedPreference.getString(requireContext(), "email")!!
        val call:Call<ArrayList<PostData>> = apiInterface.getPostData(email, index)
        call.enqueue(object : Callback<ArrayList<PostData>>{
            override fun onFailure(call: Call<ArrayList<PostData>>, t: Throwable) {

                Log.d("콜에러", t.message)
            }

            override fun onResponse(
                call: Call<ArrayList<PostData>>,
                response: Response<ArrayList<PostData>>
            ) {

                if(response.isSuccessful){
                    response.body()?.let { postDataList.addAll(it) }
                    adapter?.notifyDataSetChanged()
                }

                if(adapter?.itemCount==0){
                    welcome?.visibility = View.VISIBLE
                    welcome2?.visibility = View.VISIBLE
                    welcome3?.visibility = View.VISIBLE
                    val fadeIn = AlphaAnimation(0.0f, 1.0f)
                    val fadeOut = AlphaAnimation(1.0f, 0.0f)
                    welcome?.startAnimation(fadeIn)
//        txtView.startAnimation(fadeOut)
                    fadeIn.setDuration(1500)
                    fadeIn.setFillAfter(false)
                    fadeOut.setDuration(1200)
                    fadeOut.setFillAfter(false)
                    fadeOut.setStartOffset(4200 + fadeIn.getStartOffset())
                    val fadeIn2 = AlphaAnimation(0.0f, 1.0f)
                    fadeIn2.startOffset = 1500
                    fadeIn2.setDuration(1500)
                    val fadeIn3 = AlphaAnimation(0.0f, 1.0f)
                    fadeIn3.startOffset = 3000
                    fadeIn3.setDuration(1500)
                    welcome2?.startAnimation(fadeIn2)
                    welcome3?.startAnimation(fadeIn3)
                } else{
                    welcome?.visibility = View.GONE
                    welcome2?.visibility = View.GONE
                    welcome3?.visibility = View.GONE
                }
            }

        })


    }

    fun loadMore(index: Int){

        val email = sharedPreference.getString(requireContext(), "email")!!
        postDataList.add(PostData("load"))
        adapter!!.notifyItemInserted(postDataList.size-1)

        val newIndex = index + 1

        val call:Call<ArrayList<PostData>> = apiInterface.getPostData(email, newIndex)
        call.enqueue(object : Callback<ArrayList<PostData>>{
            override fun onFailure(call: Call<ArrayList<PostData>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<PostData>>,
                response: Response<ArrayList<PostData>>
            ) {
                if(response.isSuccessful){
                    postDataList.removeAt(postDataList.size - 1)
                    val result: ArrayList<PostData>? = response.body()
                    if(result!!.size > 0) {
                        postDataList.addAll(result)
                    }else {
                        adapter!!.isMoreDataAvailable = false
                    }
                    adapter!!.notifyDataChanged()
                }
            }
        })
    }


//    fun update(email: String){
//
//        val call: Call<User> = apiInterface.update(email)
//        call.enqueue(object: Callback<User>{
//            override fun onFailure(call: Call<User>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//
//                likeNumber = response.body()?.likeNumber!!
//                followNumber = response.body()?.followerNumber!!
//                postNumber = response.body()?.postNumber!!
//
//
//            }
//
//        })
//
//    }
}

