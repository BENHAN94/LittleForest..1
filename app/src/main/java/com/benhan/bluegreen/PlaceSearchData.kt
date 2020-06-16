package com.benhan.bluegreen

import android.net.Uri
import com.google.gson.annotations.SerializedName

class PlaceSearchData(@SerializedName("kind")var kind: String){

    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("name")
    var name:String? =null
    @SerializedName("province")
    var province:String? = null
    @SerializedName("type")
    var type:String? = null
    @SerializedName("photo")
    var photo: Uri? = null


}