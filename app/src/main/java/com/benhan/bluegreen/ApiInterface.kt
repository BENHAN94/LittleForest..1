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

}