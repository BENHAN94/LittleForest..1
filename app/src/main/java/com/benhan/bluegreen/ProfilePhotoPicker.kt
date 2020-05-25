package com.benhan.bluegreen

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class ProfilePhotoPicker: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_photo_picker)


        val recyclerView = findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val selectedView = findViewById<com.theartofdev.edmodo.cropper.CropImageView>(R.id.selectedImage)



        val getImageUri = GetImageUri(this)
        val list = getImageUri.getImageUri()
        val galleryAdapter = GalleryAdapter(this, list)
        val slidingLayout = findViewById<SlidingUpPanelLayout>(R.id.sliding_layout)

        slidingLayout.coveredFadeColor = 0




        val ivX = findViewById<ImageView>(R.id.ivX)
        val ivNext = findViewById<TextView>(R.id.plusNext)

        ivX.setOnClickListener {

            finish()



        }




        val mOnItemClickListener = object : OnItemClickListener{


            override fun OnItemClick(viewHolder: GalleryAdapter.Holder, position: Int) {
                val photoVO:PhotoVO = galleryAdapter.list[position]
                val photoVOSelectedBefore: PhotoVO? = galleryAdapter.list.find {
                    it.selected
                }


                if (photoVO.selected){

                    if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN ||
                        slidingLayout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED ||
                        slidingLayout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED){

                        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

                    }


                    if(slidingLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED){


                        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                    }







                }else{


                    if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN ||
                        slidingLayout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED ||
                        slidingLayout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED){

                        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

                    }

                    photoVOSelectedBefore?.selected = false
                    photoVO.selected = true

//                    Glide.with(this@ProfilePhotoPicker).load(photoVO.imgPath)
//                        .thumbnail(0.1F)
//                        .centerCrop()
//                        .into(selectedView)

                    selectedView.setImageUriAsync(photoVO.imgPath)




                }
                galleryAdapter.list[position] = photoVO
                galleryAdapter.notifyDataSetChanged()

                ivNext.setOnClickListener {

                    onNextClicked(galleryAdapter.list[position])

                }

            }


        }


        galleryAdapter.list[0].selected
//        Glide.with(this).load(galleryAdapter.list[0].imgPath).thumbnail(0.1F)
//            .centerCrop().into(selectedView)
        selectedView.setImageUriAsync(galleryAdapter.list[0].imgPath)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        galleryAdapter.onItemClickListener = mOnItemClickListener
        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)


        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = galleryAdapter
        recyclerView.addItemDecoration(dividerDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()


    }




    fun onNextClicked(selectedPhoto: PhotoVO, cropImageView: com.theartofdev.edmodo.cropper.CropImageView) {



        val passSelectedPhoto = ViewModelProvider(this)[PassSelectedPhoto::class.java]
        passSelectedPhoto.passSelectedPhoto(selectedPhoto)


        finish()



        cropImageView.setOnCropImageCompleteListener(object :com.theartofdev.edmodo.cropper.CropImageView.OnCropImageCompleteListener{

            override fun onCropImageComplete(
                view: com.theartofdev.edmodo.cropper.CropImageView?,
                result: com.theartofdev.edmodo.cropper.CropImageView.CropResult?
            ) {

            }
        })

        cropImageView.getCroppedImageAsync()







    }


}