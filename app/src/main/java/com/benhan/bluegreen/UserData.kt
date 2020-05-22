package com.benhan.bluegreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserData: ViewModel() {

    val userData = MutableLiveData<User>()



    fun saveUserData(userInfo: User){

        userData.value = userInfo


    }

}