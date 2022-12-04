package com.lee.foodi.ui.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lee.foodi.R
import com.lee.foodi.ui.activities.splash.SplashActivity

private const val TAG = "TimerReceiver"
private const val CHANNEL_ID = "notification_channel_id"
private const val CHANNEL_NAME = "notification_channel_name"
private const val REQUEST_CODE = 101

class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activityStartIntent = Intent(context , SplashActivity::class.java)

        val notificationBuilder : NotificationCompat.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID , CHANNEL_NAME , NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            NotificationCompat.Builder(context , CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }

        val pendingIntent : PendingIntent =
            PendingIntent.getActivity(context , REQUEST_CODE , activityStartIntent , PendingIntent.FLAG_UPDATE_CURRENT)
        with(notificationBuilder){
            setContentTitle(context.getString(R.string.app_name))
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentIntent(pendingIntent)
        }
        val notification = notificationBuilder.build()
        notificationManager.notify(1, notification)
    }
}