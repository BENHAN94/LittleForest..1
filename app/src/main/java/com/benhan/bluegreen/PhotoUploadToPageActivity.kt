package com.benhan.bluegreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.daimajia.numberprogressbar.NumberProgressBar
import com.daimajia.numberprogressbar.OnProgressBarListener
import de.hdodenhof.circleimageview.CircleImageView
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
import java.io.FileInputStream
import java.io.FileOutputStream


class PhotoUploadToPageActivity: AppCompatActivity(), ProgressRequestBody.UploadCallbacks, OnProgressBarListener{



    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val sharedPreference = SharedPreference()
    var desc = ""
    var responseListener: ResponseListener? = null




    var file: File? = null
    var email : String? = null
    var placeName: String ? = null
    var placePhoto : String? = null
    var placeType: String? = null
    var placeProvince: String? =null
    var postNumber: Int? = null
    var followerNumber: Int? = null
    var likeNumber: Int? = null
    var placeId: Int? = null
    var progressBar: NumberProgressBar? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_page_upload)


        email = sharedPreference.getString(this, "email")
        placeId = intent.getIntExtra("place_id", 0)
        val myName = sharedPreference.getString(this, "name")
        val myPhoto = sharedPreference.getString(this, "profilePhoto")


        progressBar = findViewById(R.id.number_progress_bar)

        val selectedPhotoString: String? = intent.getStringExtra("photo")
        val selectedPhoto: String = Uri.parse(selectedPhotoString).path!!
        val writeCommentContainer: LinearLayout = findViewById(R.id.writeCommentContainer)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager





        val email = sharedPreference.getString(this, "email")


        val ivSelectedPhoto = findViewById<ImageView>(R.id.selectedImageUpload)
        val etDescription: EditText = findViewById(R.id.writeComment)
        val tvDescription = findViewById<TextView>(R.id.postDescription)
        val tvPost =findViewById<TextView>(R.id.post)
        val ivPlacePhoto = findViewById<CircleImageView>(R.id.placePhoto)
        val tvPostNumber = findViewById<TextView>(R.id.postNumber)
        val tvFollowerNumber = findViewById<TextView>(R.id.followerNumber)
        val tvLikesNumber = findViewById<TextView>(R.id.likeNumber)
        val tvPlaceName = findViewById<TextView>(R.id.placeName)
        val tvPlaceProvince = findViewById<TextView>(R.id.placeProvince)
        val tvPlaceType = findViewById<TextView>(R.id.placeType)
        val ivMyProfilePhoto = findViewById<CircleImageView>(R.id.userProfilePhoto)
        val tvMyName = findViewById<TextView>(R.id.userName)
        val tvTopPlaceName = findViewById<TextView>(R.id.topPlaceName)
        val myProfilePhoto: CircleImageView = findViewById(R.id.myProfilePhoto)



        tvDescription.setOnClickListener {
            etDescription.requestFocus()
            imm.showSoftInput(etDescription, 0)
            writeCommentContainer.visibility = View.VISIBLE
        }



        Glide.with(this@PhotoUploadToPageActivity).load(selectedPhoto).fitCenter()
            .into(ivSelectedPhoto)


        val actualImageFile = File(selectedPhoto)
        file = saveBitmapToFile(actualImageFile)

        fun getPlaceData(placeId: Int){

            val callGetPageInfo: Call<PlacePageData> = apiInterface.getPageInfo(placeId)
            callGetPageInfo.enqueue(object : Callback<PlacePageData>{
                override fun onFailure(call: Call<PlacePageData>, t: Throwable) {

                }

                override fun onResponse(call: Call<PlacePageData>, response: Response<PlacePageData>) {
                    postNumber = response.body()!!.postNumber
                    followerNumber = response.body()!!.followerNumber
                    likeNumber = response.body()!!.likeNumber
                    placePhoto = response.body()!!.photo
                    placeName = response.body()!!.name
                    placeProvince = response.body()!!.province
                    placeType = response.body()!!.type

                    val placePhotoUri = MyApplication.severUrl+placePhoto
                    val myPhotoUri = MyApplication.severUrl+myPhoto
                    Glide.with(this@PhotoUploadToPageActivity).load(placePhotoUri)
                        .centerCrop()
                        .into(ivPlacePhoto)
                    Glide.with(this@PhotoUploadToPageActivity).load(myPhotoUri)
                        .override(ivMyProfilePhoto.width, ivMyProfilePhoto.height)
                        .into(ivMyProfilePhoto)
                    Glide.with(this@PhotoUploadToPageActivity).load(myPhotoUri)
                        .override(ivMyProfilePhoto.width, ivMyProfilePhoto.height)
                        .into(myProfilePhoto)


                    tvPostNumber.text = postNumber?.toString()
                    tvFollowerNumber.text = followerNumber?.toString()
                    tvLikesNumber.text = likeNumber?.toString()
                    tvPlaceName.text = placeName
                    tvPlaceProvince.text = placeProvince
                    tvPlaceType.text = placeType
                    tvMyName.text = myName
                    tvTopPlaceName.text = placeName
                }


            })
        }



        getPlaceData(placeId!!)






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
                    hideKeyboard(this@PhotoUploadToPageActivity)
                    tvDescription.text = etDescription.text
                    val handler = Handler()
                    handler.postDelayed({
                        writeCommentContainer?.visibility = View.GONE
                    }, 200)
                }
                return false
            }


        })



        ivSelectedPhoto.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                hideKeyboard(this@PhotoUploadToPageActivity)
                etDescription.clearFocus()
                val handler = Handler()
                handler.postDelayed({
                    writeCommentContainer?.visibility = View.GONE
                }, 300)
                return false
            }


        })




       tvPost.setOnClickListener {
            uploadToServer(desc)
        }





        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {
           finish()
            hideKeyboard(this)
        }






    }






    private fun uploadToServer(desc: String) {


        progressBar?.visibility = View.VISIBLE
        val fileBody = ProgressRequestBody(file!!,"image", this)
        val filePart = MultipartBody.Part.createFormData("file", file?.name, fileBody)
//        val requestBody = file!!.asRequestBody("image/*".toMediaTypeOrNull())
//            val fileToUpload = MultipartBody.Part.createFormData("file", file!!.name, requestBody)
            val filename = file!!.name.toRequestBody("text/plain".toMediaTypeOrNull())

            val mEmail = email!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val mdesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())


            val callUpload: Call<ServerResonse> = this@PhotoUploadToPageActivity.apiInterface.uploadImage(
                filePart, placeId!!, filename, mEmail,
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

                    val intent = Intent(this@PhotoUploadToPageActivity, PlacePage::class.java)
                    intent.putExtra("placeName", placeName)
                    intent.putExtra("placeId", placeId!!)
                    intent.putExtra("placePhoto", placePhoto)
                    intent.putExtra("placeType", placeType)
                    intent.putExtra("placeProvince", placeProvince)
                    startActivity(intent)
                    finish()
                    responseListener?.onResponse()
                    Log.d("코드", response.message())


                }


            })





    }

    override fun onProgressUpdate(percentage: Int) {
        progressBar?.progress = percentage
    }


    override fun onError() {

    }

    override fun onFinish() {


    }
    override fun onProgressChange(current: Int, max: Int) {

    }

    private fun saveBitmapToFile(file:File): File{
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6

        var inputStream = FileInputStream(file)
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        val REQUIRED_SIZE=75
        var scale = 1
        while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
            o.outHeight / scale / 2 >= REQUIRED_SIZE){
            scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)

        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null ,o2)
        inputStream.close()

        file.createNewFile()
        val outputStream = FileOutputStream(file)
        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        return file
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