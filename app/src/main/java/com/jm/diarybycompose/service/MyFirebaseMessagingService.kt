package com.jm.diarybycompose.service

import android.util.Log
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken() token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived() remoteMessage: $message")
    }
}