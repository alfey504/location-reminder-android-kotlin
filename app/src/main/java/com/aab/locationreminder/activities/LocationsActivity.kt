package com.aab.locationreminder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.aab.locationreminder.databinding.ActivityLocationsBinding
import com.aab.locationreminder.utils.LocationHandler

class LocationsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LocationsActivity"
    }

    private lateinit var binding: ActivityLocationsBinding
    private lateinit var locationHandler: LocationHandler

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

        locationHandler.requestForPermissions()
    }
}