package com.example.notificationsdk.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.notificationsdk.NotificationSdk

const val BASE_URL = "http://10.0.2.2:5000/"
const val EXTRA_USER_TYPE = "user_type"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NotificationSdk.initialize(BASE_URL)

        val radioGroup = findViewById<RadioGroup>(R.id.rgUserType)

        findViewById<Button>(R.id.btnHome).setOnClickListener {
            startActivity(
                Intent(this, HomeScreenActivity::class.java)
                    .putExtra(EXTRA_USER_TYPE, selectedUserType(radioGroup))
            )
        }

        findViewById<Button>(R.id.btnShop).setOnClickListener {
            startActivity(
                Intent(this, ShopScreenActivity::class.java)
                    .putExtra(EXTRA_USER_TYPE, selectedUserType(radioGroup))
            )
        }

        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(
                Intent(this, ProfileScreenActivity::class.java)
                    .putExtra(EXTRA_USER_TYPE, selectedUserType(radioGroup))
            )
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(
                Intent(this, SettingsScreenActivity::class.java)
                    .putExtra(EXTRA_USER_TYPE, selectedUserType(radioGroup))
            )
        }
    }

    private fun selectedUserType(rg: RadioGroup): String =
        if (rg.checkedRadioButtonId == R.id.rbPremium) "premium" else "regular"
}
