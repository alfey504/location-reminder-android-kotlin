package com.aab.locationreminder.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.view.ContentInfoCompat.Flags
import com.aab.locationreminder.activities.ReminderActivity
import com.aab.locationreminder.broadcast_receivers.GeofenceBroadcastReceiver
import com.aab.locationreminder.broadcast_receivers.LocationCheckBroadcastReceiver
import com.aab.locationreminder.utils.*
import kotlinx.coroutines.*
import kotlin.math.pow
import kotlin.math.roundToInt

class LocationCheckService : Service() {

    companion object {
        const val TAG = "LocationCheckService"
    }

    private lateinit var locationHandler: LocationHandler
    private var notified: Boolean = false
    private var waitTime: Long = 1000
    private lateinit var geofenceHandler: GeofenceHandler

    override fun onCreate() {
        super.onCreate()
    }

    private fun addGeofence() {
        val morrishRoad = Location("")
        morrishRoad.latitude = 43.7904
        morrishRoad.longitude = -79.1735

        val scarboborAtEllsmere = Location("")
        morrishRoad.latitude = 43.791031
        morrishRoad.longitude =  -79.170126

        val morrishGeofenceElementList = mutableListOf(
            GeofenceElement("morrish", morrishRoad, 500.0f),
            GeofenceElement("Scarboro Ave At Ellsmere", scarboborAtEllsmere, 500.0f)
        )

        val pendingIntent: PendingIntent by lazy {
            val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        geofenceHandler = GeofenceHandlerBuilder(this)
            .addGeofenceList(morrishGeofenceElementList)
            .addPendingIntent(pendingIntent)
            .start()
        
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationHandler = LocationHandler(this)
        addGeofence()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}