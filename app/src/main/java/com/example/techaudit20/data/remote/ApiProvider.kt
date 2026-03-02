package com.example.techaudit20.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    fun create(baseUrl: String): TechAuditApi {

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl) // EJ: "https://TU_ID.mockapi.io/api/v1/"
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TechAuditApi::class.java)
    }
}