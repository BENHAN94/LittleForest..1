package com.benhan.bluegreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserLiveData: ViewModel() {

    val userData = MutableLiveData<UserDataClass>()


    fun saveUserData(userInfo: UserDataClass){

        userData.value = userInfo


    }

}