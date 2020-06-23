package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.icu.text.CompactDecimalFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.zelory.compressor.Compressor
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






    val places = ArrayList<PlaceSearchData>()
    val adapter = SearchRecyclerAdapter(this, places)
    var keyword: String = ""
    var id: Int? = null
    var file: File? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plus_fragment_gallery_upload)




        val selectedPhotoString: String? = intent.getStringExtra("photo")
        val selectedPhoto: String = Uri.parse(selectedPhotoString).path!!

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)



        val recyclerView = findViewById<RecyclerView>(R.id.searchRecycler)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.setOnTouchListener(object : View.OnTouchListener{
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
        val etDescription = findViewById<EditText>(R.id.descriptionUpload)


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

        etDescription.isFocusable = false
        searchBar.isFocusable = false

//        searchBar.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//
//
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//
//
//
//
//            }
//
//
//        })















        searchBar.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBar.clearFocus()
                    hideKeyboard(this@PhotoUploadActivity)
                    etDescription.isFocusable = false
                    searchBar.isFocusable = false
                    keyword = searchBar.text.toString()


                    if (keyword.isNullOrEmpty()){
                        keyword = ""}
                    places.removeAll(places)
                    recyclerView.removeAllViews()
                    adapter.notifyDataChanged()
                    load(keyword, 0)
                }
                return false
            }


        })

        recyclerView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                hideKeyboard(this@PhotoUploadActivity)
                etDescription.clearFocus()
                searchBar.clearFocus()
                etDescription.isFocusable = false
                searchBar.isFocusable = false

                return false
            }


        })





        etDescription.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                desc = etDescription.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })





        etDescription.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    etDescription.clearFocus()
                    hideKeyboard(this@PhotoUploadActivity)
                    etDescription.isFocusable = false
                    searchBar.isFocusable = false
                }
                return false
            }


        })

        etDescription.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                etDescription.isFocusableInTouchMode = true
                return false
            }


        })
        searchBar.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                searchBar.isFocusableInTouchMode = true
                return false
            }


        })


        ivSelectedPhoto.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                hideKeyboard(this@PhotoUploadActivity)
                etDescription.clearFocus()
                etDescription.isFocusable = false
                searchBar.isFocusable = false
                return false
            }


        })

        Glide.with(this).load(selectedPhoto).centerCrop()
            .into(ivSelectedPhoto)




        // POST Image file to Server

//
//        var selectedPhotoPath: String? = null
//        val file = File(selectedPhoto)

//        val cursor: Cursor? = this.contentResolver.query(
//            selectedPhoto, null, null, null, null
//        )
//        if (cursor !== null) {
//            cursor.moveToFirst()
//            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            selectedPhotoPath = cursor.getString(idx)
//        }





       tvPost.setOnClickListener {







            uploadToServer(selectedPhoto!!, email!!, desc!!, formattedDate)



            Log.d("시간 ", formattedDate)

            startActivity(Intent(this, HomeActivity::class.java))

        }







        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {



           finish()

            hideKeyboard(this)

        }




            var index = 0
                load(keyword, index)

            adapter.addLoadMoreListener(object: SearchRecyclerAdapter.OnLoadMoreListener{
                override fun onLoadMore() {

                    recyclerView.post(object : Runnable{
                        override fun run() {

                            index = places.size - 1

                            loadMore(keyword, index)

                        }

                    })

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
            val mDate = date.toRequestBody("text/plain".toMediaTypeOrNull())
            val mdesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())


            val callUpload: Call<ServerResonse> = this@PhotoUploadActivity.apiInterface.uploadImage(
                fileToUpload, id!!, filename, mEmail,
                mDate, mdesc
            )
            callUpload.enqueue(object : Callback<ServerResonse> {
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    Log.d("에러 ", t.message)

                }

                override fun onResponse(
                    call: Call<ServerResonse>,
                    response: Response<ServerResonse>
                ) {


                    Log.d("코드", response.message())


                }


            })

        } }

//
//
//        val callPostData: Call<ServerResonse> = this.apiInterface.uploadPostData(email,
//        date, desc)
//
//        callPostData.enqueue(object : Callback<ServerResonse>{
//            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
//                Log.d("에러 ", t.message)
//            }
//
//            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {
//
//                Log.d("유저데이터", response.message())
//
//
//            }
//
//
//        })





    }



}