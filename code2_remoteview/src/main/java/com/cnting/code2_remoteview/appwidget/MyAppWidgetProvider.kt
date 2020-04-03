package com.cnting.code2_remoteview.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.cnting.code2_remoteview.R

/**
 * Created by cnting on 2020/4/1
 *
 */
class MyAppWidgetProvider : AppWidgetProvider() {
    private val CLICK_ACTION = "CLICK"
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("===>", "onReceive,action:${intent?.action},thread:${Thread.currentThread().name}")
        if (CLICK_ACTION == intent?.action) {
            Toast.makeText(context, "click it", Toast.LENGTH_SHORT).show()
            Thread(Runnable {
                //bitmap一开始加载为空，原因看这里 https://hymane.itscoder.com/bitmap-factory-decode-return-null/
                val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

                val appWidgetManager = AppWidgetManager.getInstance(context)
                for (i in 0..36) {
                    val degree = (i * 10) % 360
                    val remoteViews = RemoteViews(context.packageName, R.layout.view_app_widget)
                    remoteViews.setImageViewBitmap(
                        R.id.appWidgetImg,
                        rotateBitmap(context, bitmap, degree)
                    )
                    val clickIntent = Intent(context, MyAppWidgetProvider::class.java)   //在android8.0及以上，不能发送隐式广播了
                    clickIntent.action = CLICK_ACTION
                    val pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0)
                    remoteViews.setOnClickPendingIntent(R.id.appWidgetImg, pendingIntent)
                    appWidgetManager.updateAppWidget(
                        ComponentName(
                            context,
                            MyAppWidgetProvider::class.java
                        ), remoteViews
                    )
                    SystemClock.sleep(30)
                }
            }).start()
        }
    }


    private fun rotateBitmap(context: Context, bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d("===>", "onUpdate,appWidgetIds.size:${appWidgetIds.size}")
        val counter = appWidgetIds.size
        for (i in 0 until counter) {
            val appWidgetId = appWidgetIds[i]
            onWidgetUpdate(context, appWidgetManager, appWidgetId)
        }
    }

    private fun onWidgetUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        Log.d("===>", "onWidgetUpdate(),appWidgetId:$appWidgetId")
        val remoteView = RemoteViews(context.packageName, R.layout.view_app_widget)
        val clickIntent = Intent(context, MyAppWidgetProvider::class.java)
        clickIntent.action = CLICK_ACTION
        val pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0)
        remoteView.setOnClickPendingIntent(R.id.appWidgetImg, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, remoteView)
    }
}