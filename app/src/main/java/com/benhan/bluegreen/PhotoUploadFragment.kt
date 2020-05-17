package com.benhan.bluegreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class PhotoUploadFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.plus_fragment_gallery_upload, container, false)
        as ViewGroup



        val passSelectedPhoto = PassSelectedPhoto()
        val selectedPhoto: PhotoVO? = passSelectedPhoto.selectedPhotoData.value

        val ivSelectedPhoto = rootView.findViewById<ImageView>(R.id.selectedImageUpload)

        Glide.with(requireContext()).load(selectedPhoto?.imgPath).centerCrop()
            .into(ivSelectedPhoto)





        val ivBack = rootView.findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {

            val navi = requireActivity().findViewById<LinearLayout>(R.id.plusNavigaiion)
            navi.visibility = View.VISIBLE

            val fragmentManager = childFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val gallery = PlusFragmentGallery()

            this@PhotoUploadFragment.onStop()

            transaction.replace(R.id.uploadLayout, gallery).commit()

        }



        return rootView

    }

}