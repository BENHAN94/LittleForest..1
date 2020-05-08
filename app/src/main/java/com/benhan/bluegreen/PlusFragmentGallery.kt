package com.benhan.bluegreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PlusFragmentGallery: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView: ViewGroup = inflater.inflate(
            R.layout.plus_fragment_gallery, container, false
        )
                as ViewGroup

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.galleryRecyclerView)

        val getImageUri = GetImageUri(requireContext())
        val list = getImageUri.getImageUri()
        val galleryAdapter = GalleryAdapter(requireContext(), list)
        val dividerDecoration = GridDividerDecoration(resources, R.drawable.divider_recyler_gallery)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.adapter = galleryAdapter
        recyclerView.addItemDecoration(dividerDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()



        return rootView


    }

}

