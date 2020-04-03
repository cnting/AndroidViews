package com.cnting.code2_remoteview

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        Log.d("===>", "正常加载bitmap:$bitmap")
        addNotify.setOnClickListener {
            addNotify()
        }
    }

    private fun addNotify() {
        val channelId = "111"
        val notificationManager = NotificationManagerCompat.from(this@MainActivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "自定义RemoteView",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val remoteView = RemoteViews(packageName, R.layout.view_notify)   //注意只有部分view可以使用
        remoteView.setTextViewText(R.id.notifyTitle, "通过remoteView修改")
        val clickIntent = Intent(this, SecondActivity::class.java)
        val clickPendingIntent =
            PendingIntent.getActivity(this, 10, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(
            R.id.notifyBtn,
            clickPendingIntent
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteView)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(clickPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}
