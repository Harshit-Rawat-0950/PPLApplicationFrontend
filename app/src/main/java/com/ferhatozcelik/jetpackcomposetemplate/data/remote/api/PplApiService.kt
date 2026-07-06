package com.ferhatozcelik.jetpackcomposetemplate.data.remote.api

import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PplApiService {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/v1/logbooks/submit")
    suspend fun submitLogbook(@Body logbook: ShiftLogbookDto): Response<Unit>

    @GET("/api/v1/logbooks")
    suspend fun getLogbooks(): Response<List<ShiftLogbookDto>>

    @Multipart
    @POST("/api/v1/nearmiss/submit")
    suspend fun submitNearMiss(
        @Part("nearMiss") nearMissJson: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Response<Unit>

    @GET("/api/v1/nearmiss")
    suspend fun getNearMisses(): Response<List<NearMissDto>>

    @PATCH("/api/v1/nearmiss/{id}/resolve")
    suspend fun resolveNearMiss(@Path("id") id: Int): Response<Unit>
}
