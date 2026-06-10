package com.example.notificationsdk

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("api/notifications")
    fun getNotifications(
        @Query("screen") screen: String,
        @Query("userType") userType: String
    ): Call<NotificationsResponse>

    @PATCH("api/notifications/{id}/view")
    fun patchView(@Path("id") id: String): Call<Void>

    @PATCH("api/notifications/{id}/click")
    fun patchClick(@Path("id") id: String): Call<Void>
}
