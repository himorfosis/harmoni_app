package com.harmonievent.network.config

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.util.concurrent.TimeUnit

class AppNetwork {

    companion object {
        // pastikan laptop(sebagai server) dan hp anda terhubung dalam 1 jaring (wifi)
        // cek ip address laptop anda dengan cmd dengan cara ipconfig
        // lihat : IPv4 Address. . . . . . . . . . . : 192.190.0.101
        // tulis IP laptop anda di IP

        val IP = "http://192.190.0.101"
        val URL = "${IP}/harmoni/"
        val API = "${URL}api/"
        val URL_IMAGE = "${URL}foto_event/"

        private val retrofitBuilder = Retrofit.Builder()

        fun <T> buildService(service: Class<T>) : T {

            val client = OkHttpClient.Builder()
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
                client.addInterceptor(this)
            }
            client.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("Accept", "application/json;charset=UTF-8")
                    .build()
                chain.proceed(request)
            }

            client.connectTimeout(2, TimeUnit.MINUTES)
            client.readTimeout(2, TimeUnit.MINUTES)

            retrofitBuilder.baseUrl(API)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

            return retrofitBuilder.build().create(service)

        }

        fun <T> createService(service: Class<T>): T {
            return retrofitBuilder.build().create(service)
        }

    }


}