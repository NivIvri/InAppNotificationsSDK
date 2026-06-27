package com.example.notificationsdk

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import coil.load

class NotificationView(
    context: Context,
    private val notification: Notification,
    private val onDismiss: () -> Unit,
    private val onActionClick: () -> Unit
) : AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_view)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setCancelable(false)

        val imageView = findViewById<ImageView>(R.id.ivNotificationImage)!!
        if (!notification.imageUrl.isNullOrBlank()) {
            imageView.visibility = View.VISIBLE
            imageView.load(notification.imageUrl)
        }

        findViewById<TextView>(R.id.tvTitle)!!.text = notification.title
        findViewById<TextView>(R.id.tvMessage)!!.text = notification.message

        findViewById<Button>(R.id.btnDismiss)!!.setOnClickListener {
            onDismiss()
            dismiss()
        }
        findViewById<Button>(R.id.btnAction)!!.setOnClickListener {
            onActionClick()
            dismiss()
        }
    }
}
