package com.benhan.bluegreen

import android.net.Uri

data class PostData(var pageProfilePhoto: Uri, var pageName: String,
                         var pageType: String, var postImage: Uri, var userProfilePhoto: Uri,
                         var userName: String, var postDescription: String, var postLikes: Int,
                         var countComments: Int, var mainComment: String, var mainCommentReply: String,
                         var postDate: String, var postId: Int, var mainCommentId: Int,
                         var mainCommentReplyId: Int, var mainCommentUserName: String, var replyUserName: String)

