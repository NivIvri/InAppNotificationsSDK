package com.example.notificationsdk.demo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notificationsdk.NotificationSdk

class SettingsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)

        findViewById<Button>(R.id.btnClearSeen).setOnClickListener {
            NotificationSdk.clearSeenNotifications(this)
            Toast.makeText(this, "Seen notifications cleared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val userType = intent.getStringExtra(EXTRA_USER_TYPE) ?: "regular"
        NotificationSdk.checkAndShow(this, "settings", userType)
    }
}
