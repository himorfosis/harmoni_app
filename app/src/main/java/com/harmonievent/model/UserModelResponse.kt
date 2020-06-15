package com.harmonievent.model

data class UserModelResponse(
    val status: Boolean,
    val `data`: List<Data>
) {
    data class Data(
        val id_user: String,
        val nama_user: String,
        val no_telp: String,
        val email: String,
        val username: String,
        val password: String
    )
}