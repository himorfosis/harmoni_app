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

//    'id_admin' => $this->post('id_admin'),
//    'id_user' => $this->post('id_user'),
//    'judul' => $this->post('judul'),
//    'deskripsi' => $this->post('deskripsi'),
//    'lokasi' => $this->post('lokasi'),
//    'tgl_mulai' => $this->post('tgl_mulai'),
//    'tgl_selesai' => $this->post('tgl_selesai'),
//    'tgl_post' => $this->post('tgl_post'),
//    'no_telp' => $this->post('no_telp'),
//    'email' => $this->post('email'),
//    'gambar' => $this->post('gambar'),
//    'status' => $this->post('status')

//    id_admin:
//    id_user:
//    judul:
//    deskripsi:
//    lokasi:
//    tgl_mulai:
//    tgl_selesai:
//    tgl_post:
//    no_telp:
//    email:
//    gambar:
//    status:

    @Multipart
    @Headers("Accept: application/json")
    @POST("event")
    fun createEvent(
        @Part photo: MultipartBody.Part,
        @PartMap text: Map<String, @JvmSuppressWildcards RequestBody>
    ): Observable<StatusModelResponse?>

    //id_event:
//    id_admin:
//    id_user:
//    judul:Sample Edit
//    deskripsi:sdas
//    lokasi:sleman
//    tgl_mulai:2020-06-09
//    tgl_selesai:2020-06-20
//    tgl_post:2020-06-05
//    no_telp:0812345678
//    email:dev@mail.com
//    status:test
//    gambar:

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