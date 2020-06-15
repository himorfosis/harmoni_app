package com.harmonievent.model

data class EventModelResponse(
    val status: Boolean,
    val data: List<Data>
) {
    data class Data(
        val id_event: String ="",
        val id_admin: String ="",
        val id_user: String ="",
        val judul: String ="",
        val deskripsi: String ="",
        val lokasi: String ="",
        val tgl_mulai: String ="",
        val tgl_selesai: String ="",
        val tgl_post: String ="",
        val no_telp: String ="",
        val email: String ="",
        val gambar: String ="",
        val status: String ="",
        val nama_user: String ="",
        val username: String ="",
        val password: String =""
    )
}