package com.lee.foodi.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lee.foodi.R
import com.lee.foodi.common.EXTRA_CODE
import com.lee.foodi.common.REQUEST_CODE
import com.lee.foodi.ui.activities.splash.SplashActivity

private const val TAG = "TimerReceiver"
private const val CHANNEL_ID = "notification_channel_id"
private const val CHANNEL_NAME = "notification_channel_name"
private const val DESCRIPTION_TEXT = "FOODI ALARM"

class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.getIntExtra(EXTRA_CODE , 0) == REQUEST_CODE){
            Log.d(TAG, "onReceive: timer is activated")
            val activityIntent = Intent(context, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(context, 101, activityIntent, 0)

            val builder : NotificationCompat.Builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createNotificationChannel(context)
                NotificationCompat.Builder(context!! , CHANNEL_ID)
            } else {
                NotificationCompat.Builder(context!!)
            }
            builder.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(context.getString(R.string.app_name))
                setContentText(context.getString(R.string.notification_timer_contents))
                priority = NotificationCompat.PRIORITY_DEFAULT
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {setChannelId(CHANNEL_ID)}
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
            NotificationManagerCompat.from(context).notify(5, builder.build())
        }
    }

    /**
     * Function for create Notification Channel when build version is over Oreo
     * **/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context : Context?) {
            val channel = NotificationChannel(CHANNEL_ID , CHANNEL_NAME , NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = DESCRIPTION_TEXT
            }

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
    }
}