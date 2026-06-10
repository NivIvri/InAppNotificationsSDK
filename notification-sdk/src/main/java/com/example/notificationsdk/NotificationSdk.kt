package com.example.notificationsdk

import android.app.Activity
import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NotificationSdk {
    private lateinit var api: NotificationApi
    private const val PREFS_NAME = "notification_sdk"

    fun initialize(baseUrl: String) {
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApi::class.java)
    }

    fun checkAndShow(activity: Activity, screen: String, userType: String) {
        api.getNotifications(screen, userType).enqueue(object : Callback<NotificationsResponse> {
            override fun onResponse(
                call: Call<NotificationsResponse>,
                response: Response<NotificationsResponse>
            ) {
                val notifications = response.body()?.notifications ?: return
                val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

                val notification = notifications.firstOrNull { n ->
                    !n.showOnce || !prefs.getBoolean(n.id, false)
                } ?: return

                activity.runOnUiThread {
                    NotificationView(
                        context = activity,
                        notification = notification,
                        onDismiss = {},
                        onActionClick = {
                            api.patchClick(notification.id).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                                override fun onFailure(call: Call<Void>, t: Throwable) {}
                            })
                        }
                    ).show()

                    if (notification.showOnce) {
                        prefs.edit().putBoolean(notification.id, true).apply()
                    }

                    api.patchView(notification.id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                        override fun onFailure(call: Call<Void>, t: Throwable) {}
                    })
                }
            }

            override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                // Silent failure - no notification shown
            }
        })
    }

    fun clearSeenNotifications(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}
