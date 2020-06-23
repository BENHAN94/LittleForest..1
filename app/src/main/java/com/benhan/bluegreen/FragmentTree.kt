package com.benhan.bluegreen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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
    var viewModel: LFVIewModel? = null
    var recyclerview: RecyclerView? = null
    var commentContainer: LinearLayout? = null
    var etWriteComment: EditText? = null
    var buttonPostComment: ImageView? = null
    var commentUserProfile: ImageView? =null
    var imm: InputMethodManager? = null
//    var onClickPostComment: OnClickPostCommentClicked? = null


    var tree: ImageView? =null
    var search : ImageView? =null
    var bell :ImageView?  =null
    var user: ImageView? = null
    var navi: LinearLayout? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val profilePhoto = sharedPreference.getString(requireContext(), "profilePhoto")





        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager





        viewModel = ViewModelProvider(requireActivity()).get(LFVIewModel::class.java)

        postDataList = viewModel!!.postDataList

        if(postDataList.size == 0)
            load()

        if(adapter == null)
            adapter = HomeRecyclerAdapter(requireContext(), requireActivity(), postDataList, profilePhoto!!)

    }

    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recyclerview = view.findViewById<RecyclerView>(R.id.treeRecyclerView)
        recyclerview!!.layoutManager = SpeedyLinearLayoutManager(requireContext())
        recyclerview!!.adapter = adapter
        val skeletonScreen = Skeleton.bind(recyclerview)
            .adapter(adapter).
            shimmer(true).
            angle(20).
            duration(1000).
            load(R.layout.layout_default_item_skeleton).
            show()

        recyclerview!!.postDelayed(Runnable { skeletonScreen.hide() }, 1000)

        val swipeLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)

        swipeLayout.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                postDataList.removeAll(postDataList)
                adapter!!.notifyDataChanged()
                load()
                swipeLayout.isRefreshing = false
            }


        })






    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView == null)
         rootView =  inflater.inflate(R.layout.home_fragment_tree, container, false)



        adapter!!.addWriteCommentClickListener(object : HomeRecyclerAdapter.OnWriteCommentClicked{
            override fun onWriteCommentClicked() {
                navi?.visibility = View.GONE

                Log.d("나비", "곤")
//                    commentContainer?.visibility = View.VISIBLE
//                    etWriteComment?.requestFocus()
//                    imm?.showSoftInput(etWriteComment, InputMethodManager.SHOW_IMPLICIT)
            }
        })
        adapter?.addOnPostClickListener(object  : HomeRecyclerAdapter.OnPostClicked{

            override fun onPostClicked(position: Int) {

                recyclerview?.smoothScrollToPosition(position)

                Log.d("나비", "비져블")
                val handler = Handler()
                handler.postDelayed(object: Runnable{
                    override fun run() {
                        navi?.visibility = View.VISIBLE
                    }
                }, 350)

            }



        })



         tree = rootView!!.findViewById(R.id.tree)
         search = rootView!!.findViewById(R.id.search)
         bell = rootView!!.findViewById(R.id.bell)
         user = rootView!!.findViewById(R.id.user)
        navi = rootView!!.findViewById(R.id.navigation_bar)
        commentContainer = rootView!!.findViewById(R.id.writeCommentContainer)
        etWriteComment = rootView!!.findViewById(R.id.etWriteComment)
        buttonPostComment = rootView!!.findViewById(R.id.postComment)
        commentUserProfile = rootView!!.findViewById(R.id.commentUserProfilePhoto)

        etWriteComment!!.isFocusableInTouchMode = true



        tree!!.setImageResource(R.drawable.tree_selected)
        search!!.setImageResource(R.drawable.search)
        bell!!.setImageResource(R.drawable.bell)
        user!!.setImageResource(R.drawable.user)




        val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionListener = object : PermissionListener {

            override fun onPermissionGranted() {
                startActivity(Intent(requireContext(), PlusActivity::class.java))

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

                viewModel!!.postDataList = postDataList
            }
        }



        clickHandler(tree!!)
        clickHandler(search!!)
        clickHandler(bell!!)
        clickHandler(user!!)


//        buttonPostComment?.setOnClickListener {
//
//            commentContainer?.visibility = View.GONE
//            navi?.visibility = View.VISIBLE
//            imm?.hideSoftInputFromWindow(etWriteComment?.windowToken, 0)
//            onClickPostComment?.sendCommentContents(etWriteComment?.text.toString())
//            etWriteComment?.text = null
//
//
//        }

        etWriteComment!!.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    commentContainer?.visibility = View.GONE
                    navi?.visibility = View.VISIBLE
                    imm?.hideSoftInputFromWindow(etWriteComment?.windowToken, 0)
//                    onClickPostComment?.sendCommentContents(etWriteComment?.text.toString())
                    etWriteComment?.text = null
                    return true
                }
                return false
            }


        })



        return rootView



    }


    fun load() {

        val email = sharedPreference.getString(requireContext(), "email")!!
        val call:Call<ArrayList<PostData>> = apiInterface.getPostData(email, 0)
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

//
//    interface OnClickPostCommentClicked{
//
//
//        fun sendCommentContents(contents: String)
//
//
//    }
//
//    fun addOnPostCommentClickListener(listener: OnClickPostCommentClicked) {
//
//        onClickPostComment = listener
//    }

}