package com.benhan.bluegreen

import com.google.gson.annotations.SerializedName

class ServerResonse {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("isInserted")
    var isInserted: Boolean? = null

    @SerializedName("insertMessage")
    var insertMessage: String? = null

}