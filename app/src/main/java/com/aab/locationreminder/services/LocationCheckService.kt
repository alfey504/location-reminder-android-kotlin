package com.aab.locationreminder.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.view.ContentInfoCompat.Flags

class LocationCheckService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}