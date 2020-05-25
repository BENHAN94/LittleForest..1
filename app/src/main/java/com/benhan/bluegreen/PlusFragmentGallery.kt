package com.benhan.bluegreen

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.bumptech.glide.Glide
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_plus.*
import kotlinx.android.synthetic.main.plus_fragment_gallery.*
import java.nio.channels.Selector


class PlusFragmentGallery: Fragment() {





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





        val rootView: ViewGroup = inflater.inflate(
            R.layout.plus_fragment_gallery, container, false
        ) as LinearLayout





        val recyclerView = rootView.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val selectedView = rootView.findViewById<ImageView>(R.id.selectedImage)


        recyclerView.setHasFixedSize(true)


        val getImageUri = GetImageUri(requireContext())
        val list = getImageUri.getImageUri()

        val galleryAdapter = GalleryAdapter(requireContext(), list)









        val slidingLayout = rootView.findViewById<SlidingUpPanelLayout>(R.id.sliding_layout)

        slidingLayout.coveredFadeColor = 0



        val ivX = rootView.findViewById<ImageView>(R.id.ivX)
        val ivNext = rootView.findViewById<TextView>(R.id.plusNext)

        ivX.setOnClickListener {

            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
        }




        galleryAdapter.list[0].selected
        Glide.with(requireContext()).load(galleryAdapter.list[0].imgPath).thumbnail(0.1F)
            .centerCrop().into(selectedView)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED







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

                    Glide.with(requireContext()).load(photoVO.imgPath)
                        .thumbnail(0.1F)
                        .centerCrop()
                        .into(selectedView)

                }
                galleryAdapter.list[position] = photoVO
                galleryAdapter.notifyDataSetChanged()

                ivNext.setOnClickListener {

                    onNextClicked(galleryAdapter.list[position])

                }

            }


        }

        galleryAdapter.onItemClickListener = mOnItemClickListener
        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.adapter = galleryAdapter
        recyclerView.addItemDecoration(dividerDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()







            return rootView


    }







    fun onNextClicked(selectedPhoto: PhotoVO) {



       val passSelectedPhoto = ViewModelProvider(requireActivity())[PassSelectedPhoto::class.java]
        passSelectedPhoto.passSelectedPhoto(selectedPhoto)


        val fragmentUpload = PhotoUploadFragment()



        val navi = requireActivity().findViewById<LinearLayout>(R.id.plusNavigaiion)
        navi.visibility = View.GONE


        val childFragManager = childFragmentManager
        val childFragTrans = childFragManager.beginTransaction()
        childFragTrans.add(R.id.plus_fragment_gallery, fragmentUpload)
        childFragTrans.addToBackStack("B")
        childFragTrans.commit()







    }






}

