package com.benhan.bluegreen

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

class PostData(@SerializedName("kind") var kind: String? = null) : Parcelable{

    //place table
    @SerializedName("place_photo")
    var pageProfilePhoto: String? =null
    @SerializedName("place_name")
    var pageName: String? =null
    @SerializedName("place_type")
    var pageType: String? =null
    @SerializedName("place_province")
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
    @SerializedName("post_place_id")
    var pageId: Int? =null


    //user table
    @SerializedName("user_photo")
    var userProfilePhoto: String? =null
    @SerializedName("user_name")
    var userName: String? =null



    //comment table
    @SerializedName("comment_contents")
    var mainComment: String? =null
    @SerializedName("comment_id")
    var mainCommentId: Int? =null
    @SerializedName("comment_user_name")
    var mainCommentUserName: String? =null


    //reply table
    @SerializedName("main_comment_reply_user_name")
    var replyUserName: String? =null
    @SerializedName("main_comment_reply_id")
    var mainCommentReplyId: Int? =null
    @SerializedName("main_comment_reply")
    var mainCommentReply: String? =null


    //likes
    @SerializedName("is_liking_post")
    var isLikingPost: Boolean? = null
    @SerializedName("is_liking_comment")
    var isLikingComment: Boolean? = null


    constructor(parcel: Parcel) : this(parcel.readString()) {
        pageProfilePhoto = parcel.readString()
        pageName = parcel.readString()
        pageType = parcel.readString()
        pageProvince = parcel.readString()
        postImage = parcel.readString()
        commentNumber = parcel.readValue(Int::class.java.classLoader) as? Int
        postDescription = parcel.readString()
        postLikes = parcel.readValue(Int::class.java.classLoader) as? Int
        postDate = parcel.readString()
        postId = parcel.readValue(Int::class.java.classLoader) as? Int
        pageId = parcel.readValue(Int::class.java.classLoader) as? Int
        userProfilePhoto = parcel.readString()
        userName = parcel.readString()
        mainComment = parcel.readString()
        mainCommentId = parcel.readValue(Int::class.java.classLoader) as? Int
        mainCommentUserName = parcel.readString()
        replyUserName = parcel.readString()
        mainCommentReplyId = parcel.readValue(Int::class.java.classLoader) as? Int
        mainCommentReply = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeString(pageProfilePhoto)
        parcel.writeString(pageName)
        parcel.writeString(pageType)
        parcel.writeString(pageProvince)
        parcel.writeString(postImage)
        parcel.writeValue(commentNumber)
        parcel.writeString(postDescription)
        parcel.writeValue(postLikes)
        parcel.writeString(postDate)
        parcel.writeValue(postId)
        parcel.writeValue(pageId)
        parcel.writeString(userProfilePhoto)
        parcel.writeString(userName)
        parcel.writeString(mainComment)
        parcel.writeValue(mainCommentId)
        parcel.writeString(mainCommentUserName)
        parcel.writeString(replyUserName)
        parcel.writeValue(mainCommentReplyId)
        parcel.writeString(mainCommentReply)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostData> {
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }
    }
}

