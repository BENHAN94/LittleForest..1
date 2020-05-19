package com.benhan.bluegreen

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import java.io.File


class PhotoUploadFragment: Fragment() {

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






        val passSelectedPhoto = ViewModelProvider(requireActivity())[PassSelectedPhoto::class.java]
        val selectedPhoto: PhotoVO? = passSelectedPhoto.selectedPhotoData.value

        val ivSelectedPhoto = rootView.findViewById<ImageView>(R.id.selectedImageUpload)

        Glide.with(requireContext()).load(selectedPhoto?.imgPath).centerCrop()
            .into(ivSelectedPhoto)



        // POST Image file to Server

        val imageFile = File(selectedPhoto!!.imgPath.path!!)

        







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

}