package com.aab.locationreminder.services

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.view.ContentInfoCompat.Flags
import com.aab.locationreminder.utils.LocationHandler
import kotlinx.coroutines.*
import java.time.Duration

class LocationCheckService : Service() {

    companion object {
        const val TAG = "LocationCheckService"
    }

    private lateinit var locationHandler: LocationHandler
    private var notified: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationHandler = LocationHandler(this)
        CoroutineScope(Dispatchers.IO).launch { locationCheck(1000) }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private suspend fun locationCheck(duration: Long){
        withContext(Dispatchers.IO){
            while (true){
                locationHandler.getLocation {
                    val morrishRoad = Location("")
                    morrishRoad.latitude = 43.7904
                    morrishRoad.longitude = -79.1735
                    val distanceMeters = it.distanceTo(morrishRoad)
                    if(distanceMeters < 1000){
                        if(!notified) {
                            Log.i(TAG, distanceMeters.toString())
                            notified = true
                        }
                    }else{
                        notified = false
                    }
                }
                delay(duration)
            }
        }
    }
}