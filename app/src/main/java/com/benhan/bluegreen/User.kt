package com.benhan.bluegreen

import com.google.gson.annotations.SerializedName

class User {


    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("birthday")
    var birthday: String? = null

}