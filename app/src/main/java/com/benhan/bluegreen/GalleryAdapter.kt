package com.benhan.bluegreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import kotlin.math.sign

class GalleryAdapter(val context: Context, allImageList: ArrayList<PhotoVO> ): RecyclerView.Adapter<GalleryAdapter.Holder>() {



    var onItemClickListener: OnItemClickListener? = null


    val list = allImageList






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.view_holder, parent, false)




        return Holder(layout)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {



        val photoVO = list[position]
        Glide.with(context).load(photoVO.imgPath)
            .thumbnail(0.1F)
            .override(100, 100)
            .centerCrop()
            .into(holder.imageViewHolder)

        if(photoVO.selected){
            holder.layoutSelect.visibility = View.VISIBLE
        } else{
            holder.layoutSelect.visibility = View.INVISIBLE
        }

        holder.imageViewHolder.setOnClickListener(object  : View.OnClickListener{

            override fun onClick(v: View?) {
                if(onItemClickListener != null){
                    onItemClickListener?.OnItemClick(holder, position)
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }






    class Holder(view: View): RecyclerView.ViewHolder(view){


        val imageViewHolder: ImageView = view.findViewById(R.id.viewHolder)
        val layoutSelect = view.findViewById<RelativeLayout>(R.id.layoutSelect)
    }

}