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
                    @Part("added_date")currentTime: RequestBody,
                    @Part("description")description: RequestBody): Call<ServerResonse>

    @FormUrlEncoded
    @POST("post.php")
    fun uploadPostData(@Field("email")email: String?,
                       @Field("added_date")currentTime: String?,
                       @Field("description")description: String?): Call<ServerResonse>




}