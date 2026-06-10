package com.example.notificationsdk

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("targetScreen") val targetScreen: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("audience") val audience: String,
    @SerializedName("showOnce") val showOnce: Boolean,
    @SerializedName("viewsCounter") val viewsCounter: Int,
    @SerializedName("clicksCounter") val clicksCounter: Int
)

data class NotificationsResponse(
    @SerializedName("notifications") val notifications: List<Notification>
)
