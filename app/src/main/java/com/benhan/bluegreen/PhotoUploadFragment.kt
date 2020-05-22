package com.benhan.bluegreen

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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


class PhotoUploadFragment: Fragment() {

    val userData = ViewModelProvider(this)[UserData::class.java]
    val apiClient = ApiClient()
    private lateinit var apiInterface: ApiInterface
    val currentTime = Calendar.getInstance().time
    var df: SimpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    var formattedDate: String = df.format(currentTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.plus_fragment_gallery_upload, container, false)
        as ViewGroup









        val tvPost = rootView.findViewById<TextView>(R.id.post)
        val passSelectedPhoto = ViewModelProvider(requireActivity())[PassSelectedPhoto::class.java]
        val selectedPhoto: PhotoVO? = passSelectedPhoto.selectedPhotoData.value
        val etDescription = rootView.findViewById<EditText>(R.id.descriptionUpload)


        val ivSelectedPhoto = rootView.findViewById<ImageView>(R.id.selectedImageUpload)

        Glide.with(requireContext()).load(selectedPhoto?.imgPath).centerCrop()
            .into(ivSelectedPhoto)


        apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)


        // POST Image file to Server


        var selectedPhotoPath: String? = null

        val cursor: Cursor? = activity!!.contentResolver.query(
            selectedPhoto!!.imgPath, null, null, null, null
        )
        if (cursor !== null) {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            selectedPhotoPath = cursor.getString(idx)
        }





        tvPost.setOnClickListener {

        uploadToServer(selectedPhotoPath!!, etDescription)

            Log.d("경로 ", selectedPhotoPath)

        }






        val ivBack = rootView.findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {

            val navi = requireActivity().findViewById<LinearLayout>(R.id.plusNavigaiion)
            navi.visibility = View.VISIBLE

            val fragmentManager = childFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val gallery = PlusFragmentGallery()

            this@PhotoUploadFragment.onStop()

            transaction.replace(R.id.uploadLayout, gallery).commit()

            hideKeyboard(requireActivity())

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


    fun uploadToServer(imgPath: String, editText: EditText) {

        val text = editText.text.toString()
        val file = File(imgPath)


        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val filename = file.name.toRequestBody("text/plain".toMediaTypeOrNull())



        val callUpload: Call<ServerResonse> = this.apiInterface.uploadImage(fileToUpload, filename)
        callUpload.enqueue(object : Callback<ServerResonse>{
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



        val callPostData: Call<ServerResonse> = this.apiInterface.uploadPostData(userData.userData.value?.email,
        formattedDate, text )

        callPostData.enqueue(object : Callback<ServerResonse>{
            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
                Log.d("에러 ", t.message)
            }

            override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                Log.d("코드", response.message())
            }


        })





    }
}