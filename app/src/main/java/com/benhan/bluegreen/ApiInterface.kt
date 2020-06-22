package com.benhan.bluegreen

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("register.php")
    fun performRegistration(@Field("email")email: String,
                            @Field("password")password: String,
                            @Field("name")name: String,
                            @Field("birthday")birthday: String): Call<User>

    @FormUrlEncoded
    @POST("login.php")
    fun performUserLogin(@Field("email")email: String,
                         @Field("password")password: String): Call<User>


    @FormUrlEncoded
    @POST("email_check.php")
    fun performEmailCheck(@Field("email")email: String): Call<User>

    @FormUrlEncoded
    @POST("name_check.php")
    fun nameCheck(@Field("name")name: String): Call<User>

    @Multipart
    @POST("post.php")
    fun uploadImage(@Part file: MultipartBody.Part,
                    @Part("placeId")id: Int,
                    @Part("file")name: RequestBody,
                    @Part("email")email: RequestBody,
                    @Part("date")currentTime: RequestBody,
                    @Part("description")description: RequestBody): Call<ServerResonse>

    @Multipart
    @POST("profile_photo.php")
    fun uploadProfilePhoto(@Part file: MultipartBody.Part,
                           @Part("email")email : RequestBody): Call<ServerResonse>

//    @FormUrlEncoded
//    @POST("get_user_profile.php")
//    fun getUserProfile(@Field("actual_name")actualname: String,
//                       @Field("name")name: String,
//                       @Field("job")job: String,
//                       @Field("introduction")introduction: String,
//                       @Field("profile_photo")profilephoto: String):Call<User>


    @FormUrlEncoded
    @POST("get_user_profile.php")
    fun getUserProfile(@Field("email")email: String):Call<User>

    @FormUrlEncoded
    @POST("update_profile.php")
    fun updateProfile(@Field("actual_name")actname: String,
                      @Field("name")name: String,
                      @Field("job")job: String,
                      @Field("introduction")introduction: String,
                      @Field("email")email: String):Call<User>


    @FormUrlEncoded
    @POST("comment_like.php")
    fun commentLikeAdd(@Field("comment_no")commentNo: Int,
                        @Field("email")email: String):Call<ServerResonse>

    @FormUrlEncoded
    @POST("comment_unlike.php")
    fun commentUnlikeAdd(@Field("comment_no")commentNo: Int,
                         @Field("email")email: String):Call<ServerResonse>


    @FormUrlEncoded
    @POST("comment_like.php")
    fun replyLikeAdd(@Field("comment_no")commentNo: Int,
                       @Field("email")email: String):Call<ServerResonse>

    @FormUrlEncoded
    @POST("comment_unlike.php")
    fun replyUnlikeAdd(@Field("comment_no")commentNo: Int,
                         @Field("email")email: String):Call<ServerResonse>

    @FormUrlEncoded
    @POST("upload_comment.php")
    fun uploadComment(@Field("email")email: String,
                      @Field("name")name: String,
                      @Field("written_date")date: String,
                      @Field("contents")contents: String,
                      @Field("post_id")postId: Int): Call<ServerResonse>

    @FormUrlEncoded
    @POST("search_place.php")
    fun searchPlace(@Field("keyword")keyword: String,
                    @Field("index")index: Int): Call<ArrayList<PlaceSearchData>>

    @FormUrlEncoded
    @POST("search_place_default.php")
    fun loadPlace(@Field("keyword")keyword: String,
                  @Field("index")index: Int): Call<ArrayList<PlaceSearchData>>

    @FormUrlEncoded
    @POST("get_post.php")
    fun getPostData(@Field("email")email: String,
                    @Field("index")index: Int): Call<ArrayList<PostData>>
    @FormUrlEncoded
    @POST("get_other_user_post.php")
    fun getOtherUserPost(@Field("email")email: String,
                    @Field("index")index: Int): Call<ArrayList<PostData>>


    @POST("update_place_best_post.php")
    fun updatePlaceBestPost(): Call<ServerResonse>

    @FormUrlEncoded
    @POST("get_random_post_image.php")
    fun getRandomPostImage(@Field("index")index: Int): Call<ArrayList<PostImageData>>

    @FormUrlEncoded
    @POST("place_page.php")
    fun getPageInfo(@Field("id")id: Int): Call<PlacePageData>

    @FormUrlEncoded
    @POST("place_page_posts.php")
    fun getPagePosts(@Field("id")id: Int): Call<ArrayList<PostImageData>>

    @FormUrlEncoded
    @POST("follow_place.php")
    fun followPlace(@Field("id")id: Int,
                    @Field("email")email: String,
                    @Field("is_following")isFollowing: Boolean): Call<ServerResonse>

    @FormUrlEncoded
    @POST("unfollow_place.php")
    fun unfollowPlace(@Field("id")id: Int,
                    @Field("email")email: String,
                    @Field("is_following")isFollowing: Boolean): Call<ServerResonse>

    @FormUrlEncoded
    @POST("check_follow.php")
    fun checkFollowing(@Field("id")id: Int,
                    @Field("email")email: String): Call<ServerResonse>

    @FormUrlEncoded
    @POST("like_place.php")
    fun likePlace(@Field("id")id: Int,
                    @Field("email")email: String,
                    @Field("is_liking")isLiking: Boolean): Call<ServerResonse>

    @FormUrlEncoded
    @POST("unlike_place.php")
    fun unlikePlace(@Field("id")id: Int,
                      @Field("email")email: String,
                      @Field("is_liking")isLiking: Boolean): Call<ServerResonse>

    @FormUrlEncoded
    @POST("check_like.php")
    fun checkLike(@Field("id")id: Int,
                       @Field("email")email: String): Call<ServerResonse>

    @DELETE("delete_post_with_null_place.php")
    fun deletePostWithNullPlace(): Call<ServerResonse>

    @FormUrlEncoded
    @POST("user_post_image.php")
    fun getUserPostImage(@Field("email")email: String): Call<ArrayList<PostImageData>>


    @FormUrlEncoded
    @POST("user_page.php")
    fun getUserPageInfo(@Field("email")email: String): Call<UserPageData>

    @FormUrlEncoded
    @POST("like_post.php")
    fun likePost(@Field("email")email: String,
                 @Field("post_id")post_id: Int): Call<ServerResonse>

    @FormUrlEncoded
    @POST("like_comment.php")
    fun likeComment(@Field("email")email: String,
                 @Field("comment_id")comment_id: Int): Call<ServerResonse>

    @FormUrlEncoded
    @POST("unlike_post.php")
    fun unLikePost(@Field("email")email: String,
                 @Field("post_id")post_id: Int): Call<ServerResonse>

    @FormUrlEncoded
    @POST("unlike_comment.php")
    fun unLikeComment(@Field("email")email: String,
                    @Field("comment_id")comment_id: Int): Call<ServerResonse>

    @FormUrlEncoded
    @POST("get_other_user_data.php")
    fun getOtherUserData(@Field("name")name: String): Call<User>

    @FormUrlEncoded
    @POST("write_comment.php")
    fun writeComment(@Field("email")email: String,
                     @Field("post_id")postId: Int,
                     @Field("user_name")userName: String,
                     @Field("contents")contents: String): Call<ServerResonse>

}