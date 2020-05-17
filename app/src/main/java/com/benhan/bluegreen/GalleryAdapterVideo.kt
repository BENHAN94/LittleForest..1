package com.benhan.bluegreen

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class GalleryAdapterVideo(val context: Context, private val list: ArrayList<VideoVO> = ArrayList() ):
    RecyclerView.Adapter<GalleryAdapterVideo.HolderVideo>() {



    var onItemClickListener: OnVideoItemClickListener? = null











    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderVideo {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video, parent, false)
        return HolderVideo(layout)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(videoHolder: HolderVideo, position: Int) {


        val videoVO = list[position]

        Glide.with(context).load(videoVO.videopath).centerCrop()
            .into(videoHolder.videoViewHolder)


        if(videoVO.selected){
            videoHolder.layoutSelect.visibility = View.VISIBLE
        } else{
            videoHolder.layoutSelect.visibility = View.INVISIBLE
        }

        videoHolder.videoViewHolder.setOnClickListener(object  : View.OnClickListener{

            override fun onClick(v: View?) {
                if(onItemClickListener != null){
                    onItemClickListener?.onVideoItemClick(videoHolder, position)
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun getSelectedVideoList(): List<VideoVO> {


        val mSelectedVideoList = ArrayList<VideoVO>()

        for(i in 0..list.size){

            val videoVO = list[i]
            if(videoVO.selected){

                mSelectedVideoList.add(videoVO)
            }

        }
        return mSelectedVideoList
    }



    class HolderVideo(view: View): RecyclerView.ViewHolder(view){


        val videoViewHolder: ImageView = view.findViewById(R.id.viewHolder_video)
        val layoutSelect = view.findViewById<RelativeLayout>(R.id.layoutSelect_video)
    }

}