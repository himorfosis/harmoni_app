package com.harmonievent.service

import com.harmonievent.model.ServiceDataModel

object ServiceData {

    val ServiceOrganizer = "EVENT ORGANIZER"
    val ServiceManagement = "EVENT MANAGEMENT"
    val ServiceEquipmentRental = "EVENT EQUIPMENT RENTAL"
    val ServiceAdvertising= "ADVERTISING"
    val ServiceMultimedia = "MULTIMEDIA SERVICE"

    fun listDataService(): List<ServiceDataModel> {

        return listOf(
            eventOrganizer(),
            eventManagement(),
            eventEquipmentRental(),
            advertising(),
            multimediaService()
        )

    }

    fun eventOrganizer(): ServiceDataModel {

        return ServiceDataModel(
            "EVENT ORGANIZER",
        "Sebagai Event Organizer kami hadir untuk membantu user secara penuh dalam\n" +
                "mengadakan suatu kegiatan. Bantuan dan dukungan ini kami berikan mulai dari\n" +
                "pengembangan ide, pematangan konsep, perencanaan, hingga eksekusi kegiatan.")

    }

    fun eventManagement(): ServiceDataModel {

        return ServiceDataModel(
            "EVENT MANAGEMENT",
            "Kami juga menawarkan jasa Event Management kepada user untuk memastikan\n" +
                    "kegiatan yang telah siap diadakan dapat berjalan dengan lancar.\n" +
                    "Perbedaannya dengan Event Organizer adalah, Event Management lebih fokus ke\n" +
                    "arah supporting dalam melakukan eksekusi kegiatan.")
    }

    fun eventEquipmentRental() : ServiceDataModel {
        return ServiceDataModel(
            "EVENT EQUIPMENT RENTAL",
            "Selama kami menjalankan bisnis ini, kami telah mensupport banyak kegiatan\n" +
                    "dengan peralatan kami. Rigging dan sound system dapat kami sediakan sesegera\n" +
                    "mungkin untuk menjamin kelangsungan acara user."
        )
    }

    fun advertising() : ServiceDataModel {
        return ServiceDataModel(
            "ADVERTISING",
            "Kami juga merambah dunia periklanan. Dengan komunikasi yang baik, kami akan\n" +
                    "jamin produk user terpasarkan sesuai atau melebihi target."
        )
    }

    fun multimediaService() : ServiceDataModel {
        return ServiceDataModel(
            "MULTIMEDIA SERVICE",
            "Kami memiliki tim multimedia yang kreatif dan ahli di bidangnya, sebuah jaminan\n" +
                    "profesional untuk menunjang produktifitas user menuju kepuasan visual."
        )
    }

}