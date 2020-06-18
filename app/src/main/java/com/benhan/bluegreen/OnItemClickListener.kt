package com.benhan.bluegreen

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {

    fun OnItemClick(viewHolder: RecyclerView.ViewHolder, position: Int)
}