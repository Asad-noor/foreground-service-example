package me.ask2asad.foreground_service_example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import me.ask2asad.foreground_service_example.R
import me.ask2asad.foreground_service_example.ui.MainActivity



class WorkerService : Service() {

    private var notificationManager: NotificationManager? = null
    private val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        Log.d("tttt", "service started...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            //it is a fake notification for the purpose of calculating total's tots history and
            //then display final notification.
            val builder = NotificationCompat.Builder(
                this,
                CHANNEL_ID
            )
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
            val notification = builder.build()
            startForeground(1, notification)
        } else {
            //it is a fake notification for the purpose of calculating total's tots history and
            //then display final notification.
            val builder = NotificationCompat.Builder(
                this,
                CHANNEL_ID
            )
                .setContentTitle(getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            val notification = builder.build()
            startForeground(1, notification)
        }
        if (intent?.getBundleExtra("toto") != null) {
            Log.d("tttt", "show notification")
            //do any long task; here I am showing notification
            showNotification()
        } else {
            Log.d("tttt", "NOT show notification")
            //if there is no need to display notification
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun showNotification() {
        val whenDisplay: Long = 0 // Now
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setWhen(whenDisplay)
            .setContentTitle("Title")
            .setContentText("This is a notification from foreground service.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setTicker("ticker")
            .setStyle(NotificationCompat.BigTextStyle().bigText("hello"))
            .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()
        getNotificationManager()?.notify(System.currentTimeMillis().toInt(), notification)

        stopSelf()
    }

    private fun getNotificationManager(): NotificationManager? {
        if (notificationManager == null) {
            notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        if (notificationManager != null) {
            notificationManager = null
        }

        Log.d("tttt", "Service onDestroy called");
        stopForeground(false)
    }
}