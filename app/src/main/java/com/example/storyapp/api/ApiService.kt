package com.example.storyapp.api

import com.example.storyapp.model.ResponseHome
import com.example.storyapp.model.ResponseLogin
import com.example.storyapp.model.ResponseRegister
import com.example.storyapp.model.ResponseUploadStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService  {
    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<ResponseRegister>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<ResponseLogin>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ) : Response<ResponseHome>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : Response<ResponseUploadStory>

}

