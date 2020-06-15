package com.harmonievent.network.service

import com.harmonievent.entity.UserModel
import com.harmonievent.model.UserModelResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("/api/details")
    fun login(): Observable<UserModel>

    @FormUrlEncoded
    @POST("user")
    fun register(
        @Field("nama_user") nama_user: String,
        @Field("no_telp") no_telp: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<UserModel>

    @GET("user")
    fun fetchUser(): Observable<UserModelResponse>

}