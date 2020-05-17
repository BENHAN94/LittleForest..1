package com.benhan.bluegreen

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GetVideoUri(context: Context) {




    val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DATE_TAKEN
    )

   val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"

    val cursor = context.contentResolver.query(

        uri,
        projection,
        null,
        null,
        sortOrder
    )

    fun dateToTimestamp(day:Int, month:Int, year:Int): Long
    = SimpleDateFormat("yyyy.MM.dd").let{
        formatter -> formatter.parse("$year.$month.$day")?.time ?: 0
    }

    fun getVideoUri(): ArrayList<VideoVO>{

        val videoList = arrayListOf<VideoVO>()

        cursor?.also {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dateTakenColumn =
                it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)

            val displayName =
                it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)

            while (it.moveToNext()){
                val id = it.getLong(idColumn)
                val dateTaken = Date(it.getLong(dateTakenColumn))
                val displayName = it.getString(displayName)
                val contentUris = ContentUris.withAppendedId(uri, id)


//                imageUriList.add(contentUris)

                val videoVO = VideoVO(contentUris, false)

                videoList.add(videoVO)






            }
        }



        return videoList



    }



//    fun getVideoDuration(context: Context): ArrayList<Long> {
//
//        val uriList = getVideoUri()
//
//        val videoDurationList = arrayListOf<Long>()
//
//
//        val retriever = MediaMetadataRetriever()
//        for (i in 0 until uriList.size) {
//
//            retriever.setDataSource("uriList[i].videopath", HashMap<String, String>())
//
//            var time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//            videoDurationList.add(time.toLong())
//        }
//
//
//        retriever.release()
//    return videoDurationList
//
//    }



}