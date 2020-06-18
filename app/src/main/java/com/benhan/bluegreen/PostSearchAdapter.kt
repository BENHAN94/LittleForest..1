package com.benhan.bluegreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

class PostSearchAdapter(val context: Context, val postDataList: ArrayList<PostData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    companion object{
        const val TYPE_POST = 0
        const val TYPE_LOAD = 1

    }

    var isLoading = false
    var isMoreDataAvailable = true
    var loadMoreListener = object : SearchRecyclerAdapter.OnLoadMoreListener{
        override fun onLoadMore() {

        }




        }
    var onItemClickListener = object : OnItemClickListener{
        override fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

        }


    }

    class PostViewHolder(view: View): RecyclerView.ViewHolder(view){


        val ivPostImage = view.findViewById<ImageView>(R.id.postImage)

    }

    class LoadHolder(view: View): RecyclerView.ViewHolder(view){


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val itemLayout = LayoutInflater.from(context).inflate(R.layout.post_search_item, parent, false)
        val loadLayout = LayoutInflater.from(context).inflate(R.layout.search_recycler_load, parent, false)
        if(viewType == TYPE_POST)
        {return PostViewHolder(itemLayout)}
        else{
            return LoadHolder(loadLayout)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if(position>= itemCount-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
        {
            isLoading = true
            loadMoreListener.onLoadMore()
        }


        val item = postDataList[position]


        if(getItemViewType(position) == TYPE_POST) {
            val postImage = item.postImage
            Glide.with(context).load(postImage)
                .thumbnail(0.1F)
                .centerCrop()
                .into((holder as PostViewHolder).ivPostImage)

            holder.ivPostImage.setOnClickListener {

                onItemClickListener.OnItemClick(holder, position)

            }


        }


    }

    override fun getItemViewType(position: Int): Int {

        if(postDataList[position].kind == "post"){
            return TYPE_POST
        }
        else{
            return TYPE_LOAD
        }

    }


    override fun getItemCount(): Int {
       return postDataList.size
    }


    fun notifyDataChanged(){
        notifyDataSetChanged()
        isLoading = false
    }

    fun addLoadMoreListener(listener: SearchRecyclerAdapter.OnLoadMoreListener?) {
        loadMoreListener = listener!!
    }
}