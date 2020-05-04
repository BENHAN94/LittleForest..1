package com.benhan.bluegreen

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Attendees.query
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.query
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GetImageUri(val context: Context): ViewModel(){


    val scope = CoroutineScope(Dispatchers.Default)


    fun getImages(): LiveData<List<String>> {
        
        return loadImages(context)
    }


    private fun loadImages(context: Context): MutableLiveData<List<String>> {

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val uriList = MutableLiveData<List<String>>()
        val cursor:Cursor? = context.getContentResolver().query(uri,
        projection,
        null,
        null,
        null)




        scope.launch {

            val imageId = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()){

                val arrayList = arrayListOf<String>()
                val getImageId = cursor.getString(imageId)
                arrayList.add(getImageId)
                uriList.postValue(arrayList)


            }





        }

        
        return uriList
        
    }
}



