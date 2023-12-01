package com.jm.diarybycompose

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.jm.diarybycompose.data.datastore.DataStoreModule
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class Application : Application() {

    private lateinit var dataStore: DataStoreModule
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val context = this.applicationContext
        dataStore = DataStoreModule(context)
        applicationScope.launch {
            dataStore.getNotificationState.collect {
                FirebaseApp.initializeApp(context)
                FirebaseMessaging.getInstance().isAutoInitEnabled = it
            }
        }
    }
}