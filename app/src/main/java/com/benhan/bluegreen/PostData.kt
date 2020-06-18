package com.benhan.bluegreen

import android.net.Uri
import com.google.gson.annotations.SerializedName

class PostData(@SerializedName("kind")
               var kind: String? = null){

    //place table
    @SerializedName("page_photo")
    var pageProfilePhoto: Uri? =null
    @SerializedName("page_best_post_id")
    var pageBestPostId: Int? =null
    @SerializedName("page_name")
    var pageName: String? =null
    @SerializedName("page_type")
    var pageType: String? =null
    @SerializedName("page_province")
    var pageProvince: String?=null

    //post table
    @SerializedName("post_path")
    var postImage: String? =null
    @SerializedName("comment_number")
    var commentNumber: Int? =null
    @SerializedName("post_description")
    var postDescription: String? =null
    @SerializedName("post_likes")
    var postLikes: Int? =null
    @SerializedName("post_date")
    var postDate: String? =null
    @SerializedName("post_id")
    var postId: Int? =null
    @SerializedName("page_id")
    var pageId: Int? =null


    //user table
    @SerializedName("user_profile_photo")
    var userProfilePhoto: Uri? =null
    @SerializedName("user_name")
    var userName: String? =null



    //comment table
    @SerializedName("main_comment")
    var mainComment: String? =null
    @SerializedName("main_comment_id")
    var mainCommentId: Int? =null
    @SerializedName("main_comment_user_name")
    var mainCommentUserName: String? =null


    //reply table
    @SerializedName("main_comment_reply_user_name")
    var replyUserName: String? =null
    @SerializedName("main_comment_reply_id")
    var mainCommentReplyId: Int? =null
    @SerializedName("main_comment_reply")
    var mainCommentReply: String? =null
}

