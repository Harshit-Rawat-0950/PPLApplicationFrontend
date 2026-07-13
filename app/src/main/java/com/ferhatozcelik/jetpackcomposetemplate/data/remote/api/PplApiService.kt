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

    @POST("/api/v1/workpermit/apply")
    suspend fun applyForWorkPermit(@Body dto: WorkPermitApplyDto): Response<Unit>

    @GET("/api/v1/workpermit")
    suspend fun getWorkPermits(): Response<List<WorkPermitDto>>

    @PATCH("/api/v1/workpermit/{id}/approve")
    suspend fun approveWorkPermit(@Path("id") id: Long, @Body dto: WorkPermitApproveDto): Response<Unit>

    @GET("/api/v1/users")
    suspend fun getUsersByRole(@Query("role") role: String?): Response<List<UserDto>>

    @PATCH("/api/v1/workpermit/{id}/verify-loto")
    suspend fun verifyLoto(@Path("id") id: Long, @Body dto: WorkPermitVerifyLotoDto): Response<Unit>

    @PATCH("/api/v1/workpermit/{id}/close")
    suspend fun closePermit(@Path("id") id: Long, @Body dto: WorkPermitCloseDto): Response<Unit>

    @PATCH("/api/v1/workpermit/{id}/reject")
    suspend fun rejectPermit(@Path("id") id: Long): Response<Unit>
}
