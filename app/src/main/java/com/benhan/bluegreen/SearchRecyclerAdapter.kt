package com.benhan.bluegreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class SearchRecyclerAdapter(val context: Context, val placeList: ArrayList<PlaceSearchData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object{
        const val TYPE_PLACE = 0
        const val TYPE_LOAD = 1

    }

    var isLoading = false
    var isMoreDataAvailable = true
    var loadMoreListener = object : OnLoadMoreListener{
        override fun onLoadMore() {

        }

    }
    var onItemClickListener: OnItemClickListener? = null

    class SearchHolder(view: View): RecyclerView.ViewHolder(view){

        val ivPlacePhoto = view.findViewById<ImageView>(R.id.photo)
        val tvPlaceName = view.findViewById<TextView>(R.id.name)
        val tvPlaceProvince = view.findViewById<TextView>(R.id.province)
        val tvPlaceType = view.findViewById<TextView>(R.id.type)
        val layoutSelected = view.findViewById<RelativeLayout>(R.id.layoutSelect)
        val layoutItem = view.findViewById<RelativeLayout>(R.id.searchItem)

    }

    class LoadHolder(view: View): RecyclerView.ViewHolder(view){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {


        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemLayout = inflater.inflate(R.layout.search_recycler_item, parent, false)
        val loadLayout = inflater.inflate(R.layout.search_recycler_load, parent, false)
        if(viewType == TYPE_PLACE) {
            return SearchHolder(itemLayout)
        }else {
            return LoadHolder(loadLayout)
        }



    }


    override fun getItemCount(): Int {

        return placeList.size


    }

    override fun getItemViewType(position: Int): Int {

        if(placeList[position].kind == "place"){
            return TYPE_PLACE
        }else{
            return TYPE_LOAD
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position>= itemCount-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)

        {
            isLoading = true
            loadMoreListener.onLoadMore()
        }

        if(getItemViewType(position)== TYPE_PLACE) {
            val item = placeList[position]



            Glide.with(context).load(item.photo).thumbnail(0.3f)
                .into((holder as SearchHolder).ivPlacePhoto)

            holder.tvPlaceName.setText(item.name)
            holder.tvPlaceProvince.setText(item.province)
            holder.tvPlaceType.setText(item.type)



            if (item.isSelected){
                holder.layoutSelected.visibility = View.VISIBLE
            }else {
                holder.layoutSelected.visibility = View.INVISIBLE
            }

            holder.layoutItem.setOnClickListener(object : View.OnClickListener{



                override fun onClick(v: View?) {
                    if(onItemClickListener != null){
                        onItemClickListener?.OnItemClick(holder, position)
                    }


                }



            })


        }




    }

    interface OnLoadMoreListener{
        fun onLoadMore()
    }

    fun notifyDataChanged(){
        notifyDataSetChanged()
        isLoading = false
    }

    fun addLoadMoreListener(listener: OnLoadMoreListener?) {
        loadMoreListener = listener!!
    }
}