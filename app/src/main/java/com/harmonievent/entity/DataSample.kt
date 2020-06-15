package com.harmonievent.entity

import com.harmonievent.model.EventModelResponse

object DataSample {

    fun EventData(): List<EventModelResponse.Data> {

        return listOf(
            EventModelResponse.Data("1", "1","1", "Wedding Satu", "", "Yogyakarta", "2020-16-08", "2020-16-08", "", "", "", "", "" ),
            EventModelResponse.Data("1", "1","1", "Wedding Dua", "", "Yogyakarta", "2020-16-08", "2020-16-08", "", "", "", "", "" ),
            EventModelResponse.Data("1", "1","1", "Wedding Empat", "", "Yogyakarta", "2020-17-08", "2020-17-08", "", "", "", "", "" ),
            EventModelResponse.Data("1", "1","1", "Wedding Lima", "", "Yogyakarta", "2020-17-08", "2020-17-08", "", "", "", "", "" )
        )

    }

    fun UserModel(): List<UserModel> {
//        var id_user: Long = 0,
//        var nama_user: String ="",
//        var no_telp: String = "",
//        var email: String = "",
//        var username: String = "",
//        var password: String = ""

        return listOf(
            UserModel(1, "elon musk", "081213131414", "elon@tesla.com", "elonmusk", "secret"),
            UserModel(2, "bill gates", "081213131414", "bill@microsoft.com", "billgates", "secret"),
            UserModel(3, "user", "081213131414", "user@mail.com", "elonmusk", "secret")
        )

    }

}