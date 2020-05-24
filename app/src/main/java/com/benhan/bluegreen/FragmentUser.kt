package com.benhan.bluegreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentUser: Fragment() {


//    val prefConfig = PrefConfig(requireContext())





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_user, container, false)

        val btnUpdate = rootView.findViewById<Button>(R.id.btnProfileUpdate)

        btnUpdate.setOnClickListener {

            val userFragmentUpdateProfile = UserFragmentUpdateProfile()

            val fragmentManager = this.childFragmentManager

            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.userFragmentLayout, userFragmentUpdateProfile).commitAllowingStateLoss()



        }

        return rootView
    }




}