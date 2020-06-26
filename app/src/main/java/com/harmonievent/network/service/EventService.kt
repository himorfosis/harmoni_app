package com.harmonievent.network.service

import com.harmonievent.model.EventModelResponse
import com.harmonievent.model.StatusModelResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EventService {

    @GET("event")
    fun fetchEvent(
    ): Observable<EventModelResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("event")
    fun createEvent(
        @Part photo: MultipartBody.Part,
        @PartMap text: Map<String, @JvmSuppressWildcards RequestBody>
    ): Observable<StatusModelResponse?>

    @FormUrlEncoded
    @POST("event")
    fun createEventWithoutImage(
        @Field("id_admin") id_admin: String,
        @Field("id_user") id_user: String,
        @Field("judul") judul: String,
        @Field("deskripsi") deskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("tgl_mulai") tgl_mulai: String,
        @Field("tgl_selesai") tgl_selesai: String,
        @Field("tgl_post") tgl_post: String,
        @Field("no_telp") no_telp: String,
        @Field("email") email: String,
        @Field("status") status: String,
        @Field("gambar") gambar: String
    ): Observable<StatusModelResponse>


}