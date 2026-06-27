package com.example.notificationsdk.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notificationsdk.NotificationSdk

class ShopScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Shop"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        val userType = intent.getStringExtra(EXTRA_USER_TYPE) ?: "regular"
        NotificationSdk.checkAndShow(this, "shop", userType)
    }
}
