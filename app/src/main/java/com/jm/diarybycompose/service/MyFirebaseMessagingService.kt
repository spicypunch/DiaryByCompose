package com.jm.diarybycompose.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jm.diarybycompose.R
import com.jm.diarybycompose.data.datastore.DataStoreModule
import com.jm.diarybycompose.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var dataStore: DataStoreModule
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken(): $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage)
        } else {
            Log.d(TAG, "onMessageReceived(): $remoteMessage")
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val context = this.applicationContext
        dataStore = DataStoreModule(context)

        val id = 0
        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, id, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "Channel ID"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        applicationScope.launch {
            dataStore.getNotificationState.collect {
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel =
                    NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
                if (it) {
                    notificationManager.notify(id, notificationBuilder.build())
                } else {
//                    notificationManager.deleteNotificationChannel(channelId)
                }
            }
        }
    }
}