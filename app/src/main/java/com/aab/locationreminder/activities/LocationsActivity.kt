package com.aab.locationreminder.activities

import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aab.locationreminder.broadcast_receivers.LocationCheckBroadcastReceiver
import com.aab.locationreminder.databinding.ActivityLocationsBinding
import com.aab.locationreminder.services.LocationCheckService
import com.aab.locationreminder.utils.GeocoderHandler
import com.aab.locationreminder.utils.LocationHandler
import com.aab.locationreminder.utils.NotificationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LocationsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LocationsActivity"
    }

    private lateinit var binding: ActivityLocationsBinding
    private lateinit var locationHandler: LocationHandler
    private lateinit var receiver: LocationCheckBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHandler = LocationHandler(this)
        locationHandler.registerRequest(this)

        locationHandler.setPermissionGranted {
            Log.i(TAG, "Permission granted for location access")
        }

        locationHandler.setPermissionDenied {
            Log.i(TAG, "Permission denied for location access")
        }

        receiver = LocationCheckBroadcastReceiver()
        val intentFilter = IntentFilter(LocationCheckBroadcastReceiver.ACTION_LOCATION_UPDATE)
        registerReceiver(receiver, intentFilter)

        receiver.setOnLocationUpdate {
            val morrishRoad = Location("")
            morrishRoad.latitude = 43.7904
            morrishRoad.longitude = -79.1735

            val distance = it.distanceTo(morrishRoad)
            binding.mainText.text = "distance to morrish road $distance meters"
        }

        locationHandler.requestForPermissions()


        val notificationHandler = NotificationHandler(this)
        notificationHandler.createNotificationChannel()

        val intent = Intent(this, LocationCheckService::class.java)
        startService(intent)
    }
}