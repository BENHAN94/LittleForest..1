package com.benhan.bluegreen

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(val context: Context, allImageList: ArrayList<Uri> ): RecyclerView.Adapter<GalleryAdapter.Holder>() {



    private val list = allImageList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return Holder(layout)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        Glide.with(context).load(list[position]).centerCrop().into(holder.imageViewHolder)


    }

    override fun getItemCount(): Int {
        return list.size
    }



    class Holder(view: View): RecyclerView.ViewHolder(view){


        val imageViewHolder: ImageView = view.findViewById(R.id.viewHolder)
    }

}