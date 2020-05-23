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

        val tv = rootView.findViewById<TextView>(R.id.nameInfo)
//        tv.text = prefConfig.readName()
        val btn = rootView.findViewById<Button>(R.id.logOut)
        btn.setOnClickListener {

//            prefConfig.writeLoginStatus(false)
//            prefConfig.writeName("User")
            val sharedPreference = SharedPreference()
            sharedPreference.clear(requireActivity())
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }

        return rootView
    }




}