package com.benhan.bluegreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.home_fragment_user.*

class FragmentUser: Fragment() {


//    val prefConfig = PrefConfig(requireContext())


    val sharedPreference = SharedPreference()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.home_fragment_user, container, false)

        val btnUpdate = rootView.findViewById<Button>(R.id.btnProfileUpdate)

        btnUpdate.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileUpdateActivity::class.java))
        }

        val tvUsername = rootView.findViewById<TextView>(R.id.profile_username)
        val tvActualname = rootView.findViewById<TextView>(R.id.actualName)
        val tvIntroduction = rootView.findViewById<TextView>(R.id.userIntroduction)
        val ivMenu = rootView.findViewById<ImageView>(R.id.profile_menu)
        val ivProfilePhoto = rootView.findViewById<ImageView>(R.id.userFragmentProfilePhoto)

        tvUsername.setText(sharedPreference.getString(requireContext(), "name"))
        tvActualname.setText(sharedPreference.getString(requireContext(), "actualName"))
        tvIntroduction.setText(sharedPreference.getString(requireContext(), "introduction"))
        val profilePhotoUri = sharedPreference.getString(requireContext(), "profilePhoto")

        Glide.with(requireActivity()).load(profilePhotoUri).thumbnail(0.3F)
            .into(ivProfilePhoto)

        ivMenu.setOnClickListener {
            sharedPreference.clear(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity2::class.java))

        }

        return rootView
    }





}