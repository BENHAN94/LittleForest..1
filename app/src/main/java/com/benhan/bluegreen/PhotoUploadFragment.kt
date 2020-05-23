package com.benhan.bluegreen

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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


    val apiClient = ApiClient()
    private lateinit var apiInterface: ApiInterface







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

        val currentTime = Calendar.getInstance().time
        var df: SimpleDateFormat = SimpleDateFormat("yyyy-MMM-dd")
        var formattedDate: String = df.format(currentTime)



        val sharedPreference = SharedPreference()
        val email = sharedPreference.getString(requireActivity(), "email")





        val tvPost = rootView.findViewById<TextView>(R.id.post)
        val passSelectedPhoto = ViewModelProvider(requireActivity())[PassSelectedPhoto::class.java]
        val selectedPhoto: PhotoVO? = passSelectedPhoto.selectedPhotoData.value
        val etDescription = rootView.findViewById<EditText>(R.id.descriptionUpload)
        var desc: String? = null


        etDescription.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                desc = etDescription.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })




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



        uploadToServer(selectedPhotoPath!!, email!!, desc!!, formattedDate)

            Log.d("시간 ", formattedDate)

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


    fun uploadToServer(imgPath: String,  email: String, desc: String, date: String) {


        val file = File(imgPath)


        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val filename = file.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val mEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val mDate = date.toRequestBody("text/plain".toMediaTypeOrNull())
        val mdesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())



        val callUpload: Call<ServerResonse> = this.apiInterface.uploadImage(fileToUpload, filename,mEmail,
            mDate, mdesc)
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