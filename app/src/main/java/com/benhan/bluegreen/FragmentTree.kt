package com.benhan.bluegreen

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class FragmentTree: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_tree, container, false)

        val getImageUri = GetImageUri(requireContext())
       val list=  getImageUri.getImageUri()


        val imageView = rootView.findViewById<ImageView>(R.id.treeImage)

        Glide.with(requireContext()).load(list[0].imgPath).into(imageView)


        return rootView


    }


}