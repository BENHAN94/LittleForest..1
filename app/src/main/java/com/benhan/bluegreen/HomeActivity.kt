package com.benhan.bluegreen

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class HomeActivity : AppCompatActivity() {





    private val TAG: String = "로그"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)





        val fragmentManager = supportFragmentManager

        val fragmentBell = FragmentBell()
        val fragmentSearch = FragmentSearch()
        val fragmentTree = FragmentTree()
        val fragmentUser = FragmentUser()

        val tree = findViewById<ImageView>(R.id.tree)
        val search = findViewById<ImageView>(R.id.search)
        val bell = findViewById<ImageView>(R.id.bell)
        val user = findViewById<ImageView>(R.id.user)
        val plus = findViewById<ImageView>(R.id.plus)


        plus.setOnClickListener {



            onClicked()

        }

        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout, fragmentTree)
            .commitAllowingStateLoss()

        fun clickHandler(view: ImageView){



            view.setOnClickListener {

                val transaction = fragmentManager.beginTransaction()

            when(view.id) {

                R.id.tree -> {
                    transaction.replace(R.id.frameLayout, fragmentTree)



                    tree.setImageResource(R.drawable.tree)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)

                }
                R.id.search -> {
                    transaction.replace(R.id.frameLayout, fragmentSearch)


                    tree.setImageResource(R.drawable.tree_empty)
                    search.setImageResource(R.drawable.search_selected)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user)

                }
                R.id.bell -> {
                    transaction.replace(R.id.frameLayout, fragmentBell)



                    tree.setImageResource(R.drawable.tree_empty)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell_selected_1)
                    user.setImageResource(R.drawable.user)
                }
                R.id.user -> {
                    transaction.replace(R.id.frameLayout, fragmentUser)


                    tree.setImageResource(R.drawable.tree_empty)
                    search.setImageResource(R.drawable.search)
                    bell.setImageResource(R.drawable.bell)
                    user.setImageResource(R.drawable.user_selected)
                }


            }

                transaction.commit()




            }



        }


        clickHandler(tree)
        clickHandler(search)
        clickHandler(bell)
        clickHandler(user)




    }

    private fun onClicked( ) {
        
        Log.d(TAG, "HomeActivity - onClicked() called")

        val intent = Intent(this, PlusActivity::class.java)

        startActivity(intent)

    }






}

























