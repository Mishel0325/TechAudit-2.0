package com.example.techaudit20.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TechAuditApi {

    @POST("sync")
    suspend fun postSync(
        @Body payload: SyncPayload
    ): Response<SyncResponse>
}