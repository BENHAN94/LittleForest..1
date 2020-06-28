package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.plus_fragment_gallery_upload.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PhotoUploadActivity: AppCompatActivity(){



    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    var etDescription: EditText? = null






    val places = ArrayList<PlaceSearchData>()
    val adapter = SearchRecyclerAdapter(this, places)
    var keyword: String = ""
    var id: Int? = null
    var file: File? = null
    var responseListener : ResponseListener? =null

    var recyclerView: RecyclerView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plus_fragment_gallery_upload)



        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("회원님과 가까운 곳을 보기 위해서는 위치 정보 접근 권한이 필요해요")
            .setDeniedMessage("언제든 [설정] > [권한] 에서 권한을 허용 하시면 가까운 곳을 보실 수 있어요")
            .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .check()





        val selectedPhotoString: String? = intent.getStringExtra("photo")
        val selectedPhoto: String = Uri.parse(selectedPhotoString).path!!

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)



        recyclerView = findViewById<RecyclerView>(R.id.searchRecycler)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter
        recyclerView?.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                hideKeyboard(this@PhotoUploadActivity)
                return false
            }


        })

        val currentTime = Calendar.getInstance().time
        var df: SimpleDateFormat = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
        var formattedDate: String = df.format(currentTime)
        val backgroundColor = ContextCompat.getColor(this, R.color.background)
        val naviColor = ContextCompat.getColor(this, R.color.navi)



        val sharedPreference = SharedPreference()
        val email = sharedPreference.getString(this, "email")

        val tvPost = findViewById<TextView>(R.id.post)
//        val passSelectedPhoto = ViewModelProvider(this)[PassSelectedPhoto::class.java]
//        val selectedPhoto: PhotoVO? = passSelectedPhoto.selectedPhotoData.value
        etDescription = findViewById<EditText>(R.id.descriptionUpload)


        tvPost.setTextColor(naviColor)
        tvPost.isClickable = false

        val ivSelectedPhoto = findViewById<ImageView>(R.id.selectedImageUpload)


        var desc = ""


        val mOnItemClickListener = object: OnItemClickListener{

            override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val place: PlaceSearchData = adapter.placeList[position]
                val placeSelectedBefore: PlaceSearchData? = adapter.placeList.find{
                    it.isSelected
                }
                tvPost.setTextColor(backgroundColor)
                tvPost.isClickable = true
                if (place.isSelected) {
                    }
                else {
                    placeSelectedBefore?.isSelected = false
                    place.isSelected = true
                    id = place.id
                }
                adapter.placeList[position] = place
                adapter.notifyDataChanged()


            }

        }

        adapter.onItemClickListener = mOnItemClickListener






















        etDescription?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                desc = etDescription?.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })









        Glide.with(this).load(selectedPhoto).centerCrop()
            .into(ivSelectedPhoto)








       tvPost.setOnClickListener {
            uploadToServer(selectedPhoto!!, email!!, desc!!, formattedDate)
            startActivity(Intent(this, HomeActivity::class.java))
        }







        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {
           finish()
            hideKeyboard(this)
        }






    }








    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {

            val gpsTracker = GpsTracker(this@PhotoUploadActivity)
            val x = gpsTracker.fetchLatitude()
            val y = gpsTracker.fetchLongtitude()


            if(adapter?.itemCount == 0)
                loadClose("",0, x, y)
            searchBar.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchBar.clearFocus()
                        hideKeyboard(this@PhotoUploadActivity)


                        keyword = searchBar.text.toString()


                        if (keyword.isNullOrEmpty()){
                            keyword = ""}
                        places.removeAll(places)
                        recyclerView?.removeAllViews()
                        adapter.notifyDataChanged()
                        loadClose(keyword, 0, x, y)
                    }
                    return false
                }


            })


        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(this@PhotoUploadActivity, "권한 거부", Toast.LENGTH_SHORT).show()


            if(adapter?.itemCount == 0)
                load("", 0)
            searchBar.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchBar.clearFocus()
                        hideKeyboard(this@PhotoUploadActivity)

                        keyword = searchBar.text.toString()


                        if (keyword.isNullOrEmpty()){
                            keyword = ""}
                        places.removeAll(places)
                        recyclerView?.removeAllViews()
                        adapter.notifyDataChanged()
                        load(keyword, 0)
                    }
                    return false
                }


            })

        }



    }


    fun load(keyword: String ,index: Int) {


        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.searchPlace(keyword, index)
        call.enqueue(object: Callback<ArrayList<PlaceSearchData>> {
            override fun onFailure(call: Call<ArrayList<PlaceSearchData>>, t: Throwable) {

                Log.d("사이즈", t.message)

            }

            override fun onResponse(
                call: Call<ArrayList<PlaceSearchData>>,
                response: Response<ArrayList<PlaceSearchData>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { places.addAll(it) }
                    adapter?.notifyDataChanged()
                    Log.d("사이즈", response.body()?.size.toString())
                    if (response.body()?.size == 30)
                        setOnLoadMoreListener()
                }
            }


        })

    }

    fun loadClose(keyword: String ,index: Int, x: Double, y: Double) {


        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.searchClosePlace(keyword, index, x, y)
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
                    if(response.body()?.size == 30 )
                        setOnLoadCloseMoreListener()
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

    fun loadCloseMore(keyword: String, index: Int, x: Double, y: Double){


        places.add(PlaceSearchData("load"))
        adapter?.notifyItemInserted(places.size-1)

        val newIndex = index + 1
        val call: Call<ArrayList<PlaceSearchData>> = apiInterface.searchClosePlace(keyword, newIndex, x, y)
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

    fun setOnLoadMoreListener(){

        val onLoadMoreListener = object : HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                recyclerView?.post(object : Runnable{
                    override fun run() {
                        val index = places.size - 1
                        loadMore(keyword , index)
                    }

                })
            }
        }

        adapter?.onLoadMoreListener = onLoadMoreListener
    }

    fun setOnLoadCloseMoreListener(){

        val onLoadMoreListener = object : HomeRecyclerAdapter.OnLoadMoreListener{
            override fun onLoadMore() {
                recyclerView?.post(object : Runnable{
                    override fun run() {
                        val index = places.size - 1
                        val gpsTracker = GpsTracker(this@PhotoUploadActivity)
                        val x = gpsTracker.fetchLatitude()
                        val y = gpsTracker.fetchLongtitude()
                        loadCloseMore(keyword , index, x, y)
                    }

                })
            }
        }

        adapter?.onLoadMoreListener = onLoadMoreListener
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





    private fun uploadToServer(imgPath: String, email: String, desc: String, date: String) {



        val actualImageFile = File(imgPath)

        runBlocking { launch {
            file = Compressor.compress(this@PhotoUploadActivity, actualImageFile)


            val requestBody = file!!.asRequestBody("image/*".toMediaTypeOrNull())
            val fileToUpload = MultipartBody.Part.createFormData("file", file!!.name, requestBody)
            val filename = file!!.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val mEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val mdesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())


            val callUpload: Call<ServerResonse> = this@PhotoUploadActivity.apiInterface.uploadImage(
                fileToUpload, id!!, filename, mEmail,
                mdesc
            )
            callUpload.enqueue(object : Callback<ServerResonse> {
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    Log.d("에러 ", t.message)

                }

                override fun onResponse(
                    call: Call<ServerResonse>,
                    response: Response<ServerResonse>
                ) {



                    responseListener?.onResponse()

                    Log.d("코드", response.message())


                }


            })

        } }





    }



}