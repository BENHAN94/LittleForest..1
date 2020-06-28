package com.benhan.bluegreen

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ethanhua.skeleton.Skeleton
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
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

    var writeCommentContainer: LinearLayout? = null
    var writeComment: EditText? = null



    var tree: ImageView? =null
    var search : ImageView? =null
    var bell :ImageView?  =null
    var user: ImageView? = null
    var navi: LinearLayout? = null

    var welcome: TextView? = null
    var welcome2: TextView? = null
    var welcome3: TextView? = null

    var email: String? = null
    var name: String?= null








    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView == null)
         rootView =  inflater.inflate(R.layout.home_fragment_tree, container, false)


        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


/////////////////////////////




        email = sharedPreference.getString(requireContext(), "email")!!
        name = sharedPreference.getString(requireContext(), "name")

        val profilePhoto = sharedPreference.getString(requireContext(), "profilePhoto")





        recyclerview = rootView?.findViewById<RecyclerView>(R.id.treeRecyclerView)
        adapter = HomeRecyclerAdapter(requireContext(), requireActivity(), postDataList, profilePhoto!!)


        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.isItemPrefetchEnabled = true
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview!!.layoutManager = linearLayoutManager





        val swipeLayout = rootView?.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.background)
        swipeLayout?.setColorSchemeColors(backgroundColor)
        swipeLayout?.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {

                postDataList.removeAll(postDataList)
               adapter?.notifyDataChanged()
                adapter?.isMoreDataAvailable = true
                load(0)
                swipeLayout.isRefreshing = false
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



        recyclerview!!.postDelayed( { skeletonScreen.hide() }, 1000)








        /////////////////////////////////////////////////////////

         tree = rootView!!.findViewById(R.id.tree)
         search = rootView!!.findViewById(R.id.search)
         bell = rootView!!.findViewById(R.id.bell)
         user = rootView!!.findViewById(R.id.user)
        navi = rootView!!.findViewById(R.id.navigation_bar)

        writeCommentContainer = rootView!!.findViewById(R.id.writeCommentContainer)
        writeComment = rootView!!.findViewById(R.id.writeComment)

        recyclerview?.itemAnimator = DefaultItemAnimator()

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
                        search?.isClickable = false
                        Navigation.findNavController(rootView!!).saveState()
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_search)

                    }
                    R.id.bell -> {
                        bell?.isClickable = false
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_bell)

                    }
                    R.id.user -> {
                        user?.isClickable = false
                        Navigation.findNavController(rootView!!).navigate(R.id.from_tree_to_user)

                    }
                }


            }
        }



        clickHandler(tree!!)
        clickHandler(search!!)
        clickHandler(bell!!)
        clickHandler(user!!)



        val responseListener = object: ResponseListener{
            override fun onResponse() {
                load(0)
            }
        }

        val photoUploadActivity = PhotoUploadActivity()
        photoUploadActivity.responseListener = responseListener


        if(adapter?.itemCount == 0)
            load(0)



        val onPageClickListener = object: HomeRecyclerAdapter.OnPageClickListener {
            override fun onPageClick(position: Int) {
                val intent = Intent(context, PlacePage::class.java)
                intent.putExtra("placeName", postDataList[position].pageName)
                intent.putExtra("placeId", postDataList[position].pageId)
                intent.putExtra("placePhoto", postDataList[position].pageProfilePhoto)
                intent.putExtra("placeType", postDataList[position].pageType)
                intent.putExtra("placeProvince", postDataList[position].pageProvince)
                requireContext().startActivity(intent)

            }

        }
        adapter?.onPageClickListener = onPageClickListener

        val onUserClickListener = object: HomeRecyclerAdapter.OnUserClickListener{
            override fun onUserClick(position: Int) {
                val intent = Intent(context, OtherUser::class.java)
                intent.putExtra("otherUserName", postDataList[position].userName)
                requireActivity().startActivity(intent)
            }


        }

        adapter?.onUserClickListener = onUserClickListener

        val onClickShowAllListener = object : HomeRecyclerAdapter.OnClickShowAllListener{
            override fun onClickShowAll(position: Int) {
                val intent = Intent(context, Comment::class.java)
                intent.putExtra("post_user_name", postDataList[position].userName)
                intent.putExtra("post_description", postDataList[position].postDescription)
                intent.putExtra("post_user_profile", postDataList[position].userProfilePhoto)
                intent.putExtra("post_date", postDataList[position].postDate)
                intent.putExtra("post_id", postDataList[position].postId!!)
                requireActivity().startActivity(intent)
            }
        }

        adapter?.onClickShowAllListener = onClickShowAllListener

        val onLikeClickListener = object : HomeRecyclerAdapter.OnLikeClickListener{

            override fun onLikeClick(position: Int) {
                if (!postDataList[position].isLikingPost!!) {
                    likePost(email!!, postDataList[position].postId!!, name!!)
                    postDataList[position].postLikes = postDataList[position].postLikes?.plus(1)
                    postDataList[position].isLikingPost = true

                } else {
                    unlikePost(email!!, postDataList[position].postId!!)
                    postDataList[position].postLikes = postDataList[position].postLikes?.minus(1)
                    postDataList[position].isLikingPost = false
                }
                adapter?.notifyItemChanged(position)
            }
        }

        adapter?.onLikeClickListener = onLikeClickListener





        adapter!!.addWriteCommentClickListener(object :
            HomeRecyclerAdapter.OnWriteCommentClicked {
            override fun onWriteCommentClicked(position: Int) {
                navi?.visibility = View.GONE
                writeCommentContainer?.visibility = View.VISIBLE
                writeComment?.requestFocus()
                imm.showSoftInput(writeComment, 0)
                Log.d("나비", "곤")



                writeComment?.setOnEditorActionListener(object : TextView.OnEditorActionListener{
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                            val comment = writeComment?.text.toString()
                            postDataList[position].commentNumber = postDataList[position].commentNumber?.plus(1)
                            postDataList[position].mainCommentUserName = name!!
                            postDataList[position].mainComment = comment
                            adapter?.notifyItemChanged(position)
                            val call: Call<java.util.ArrayList<CommentData>> =
                                apiInterface.writeComment(
                                    email!!,
                                    postDataList[position].postId!!,
                                    name!!,
                                    comment)
                            val handler = Handler()
                            handler.postDelayed(object : Runnable {
                                override fun run() {
                                    navi?.visibility = View.VISIBLE
                                    writeCommentContainer?.visibility = View.GONE
                                }
                            }, 350)


                            call.clone().enqueue(object : Callback<java.util.ArrayList<CommentData>> {
                                override fun onFailure(call: Call<java.util.ArrayList<CommentData>>, t: Throwable) {

                                }

                                override fun onResponse(
                                    call: Call<java.util.ArrayList<CommentData>>,
                                    response: Response<java.util.ArrayList<CommentData>>
                                ) {

                                }


                            })
                            writeComment?.setText(null)
                            writeComment?.clearFocus()
                            UIUtil.hideKeyboard(activity)
                        }
                        return false
                    }


                })


            }


        })




        return rootView



    }


































    fun load(index: Int) {


        val call:Call<ArrayList<PostData>> = apiInterface.getPostData(email!!, index)
        call.enqueue(object : Callback<ArrayList<PostData>>{
            override fun onFailure(call: Call<ArrayList<PostData>>, t: Throwable) {

                Log.d("콜에러", t.message)
            }

            override fun onResponse(
                call: Call<ArrayList<PostData>>,
                response: Response<ArrayList<PostData>>
            ) {

                if(response.isSuccessful) {

                        response.body()?.let { postDataList.addAll(it) }
                    recyclerview!!.adapter = adapter
                        adapter?.notifyDataChanged()


                        if(postDataList.size == 10) {
                            adapter!!.addLoadMoreListener(object :
                                HomeRecyclerAdapter.OnLoadMoreListener {
                                override fun onLoadMore() {
                                    recyclerview!!.post(object : Runnable {
                                        override fun run() {
                                            val index = postDataList.size - 1
                                            loadMore(index)
                                        }

                                    })
                                }

                            })
                        }


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

    fun likePost(email: String, postId: Int, user_name: String) {

        val call: Call<ServerResonse> = apiInterface.likePost(email, postId, user_name)
        call.enqueue(object : Callback<ServerResonse> {
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ServerResonse>,
                response: Response<ServerResonse>
            ) {


            }
        })


    }

    private fun unlikePost(email: String, postId: Int) {

        val call: Call<ServerResonse> = apiInterface.unLikePost(email, postId)
        call.enqueue(object : Callback<ServerResonse> {
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ServerResonse>,
                response: Response<ServerResonse>
            ) {

            }
        })

    }
}

