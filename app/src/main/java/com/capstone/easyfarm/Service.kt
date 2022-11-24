package com.capstone.easyfarm

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @Multipart
    @POST("prediction")
    fun prediction(
        @Part image: MultipartBody.Part,
        @Part("plantType") plantType: String
    ): Call<PhotoSelectActivity.ResponseDTO_Flask>

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("PostUser")
    fun PostUser(
        @Field("deviceId") deviceId: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("alarm")
    fun alarm(
        @Field("deviceId") deviceId: String
    ): Call<ResponseBody>

    @Multipart
    @POST("PostResult")
    fun PostResult(
        @Part image: MultipartBody.Part,
        @Part("deviceId") deviceId: String,
        @Part("pestName") pestName: String,
        @Part("pestPercentage") pestPercentage: Double,
        @Part("date") date: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("GetResult")
    fun GetResult(
        @Field("deviceId") deviceId: String
    ): Call<List<Fragment2.results>>

    @FormUrlEncoded
    @POST("deleteResult")
    fun deleteResult(
        @Field("imgUrl") imgUrl: String
    ): Call<ResponseBody>

}