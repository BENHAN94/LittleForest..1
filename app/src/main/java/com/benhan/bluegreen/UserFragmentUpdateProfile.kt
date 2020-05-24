package com.benhan.bluegreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class UserFragmentUpdateProfile: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.user_fragment_update_profile, container, false)

        val tvCancel = rootView.findViewById<TextView>(R.id.profileUpdateCancel)

        tvCancel.setOnClickListener {

            val fragmentUser = FragmentUser()

            val fragmentManager = childFragmentManager

            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.updateProfileLayout, fragmentUser)

        }

        val changeProfilePhoto = rootView.findViewById<TextView>(R.id.changeProfilePhoto)

        changeProfilePhoto.setOnClickListener {

        }


        return rootView

    }
}