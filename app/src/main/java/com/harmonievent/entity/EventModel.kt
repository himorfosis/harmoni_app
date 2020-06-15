package com.harmonievent.entity

class EventModel (
    var id_event: Long = 0,
    var id_admin: Long = 0,
    var id_user: Long = 0,
    var judul: String ="",
    var deskripsi: String ="",
    var lokasi: String ="",
    var tgl_mulai: String ="",
    var tgl_selesai: String ="",
    var tgl_post: String ="",
    var no_telp: String ="",
    var email: String ="",
    var gambar: String ="",
    var status: String =""
)