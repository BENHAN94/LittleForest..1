package com.benhan.bluegreen

import com.google.gson.annotations.SerializedName

class PostImageData(@SerializedName("kind")var kind: String? = null) {


    @SerializedName("post_img")
    var postImg: String? = null
    @SerializedName("id")
    var postId: Int? = null
}