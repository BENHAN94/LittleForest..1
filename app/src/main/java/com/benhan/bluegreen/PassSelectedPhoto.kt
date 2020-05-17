package com.benhan.bluegreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassSelectedPhoto: ViewModel() {


     val selectedPhotoData =  MutableLiveData<PhotoVO>()

    fun passSelectedPhoto(selectedPhoto: PhotoVO){

        selectedPhotoData.value = selectedPhoto


    }

}