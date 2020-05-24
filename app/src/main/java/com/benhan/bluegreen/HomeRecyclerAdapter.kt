package com.benhan.bluegreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeRecyclerAdapter: RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val layout: RelativeLayout): RecyclerView.ViewHolder(layout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val treeRecyclerRow = LayoutInflater.from(parent.context)
            .inflate(R.layout.tree_recycler_row, parent, false) as RelativeLayout


        return MyViewHolder(treeRecyclerRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {


        return 0

    }



}