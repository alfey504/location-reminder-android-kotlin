package com.aab.locationreminder.activities

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aab.locationreminder.databinding.ActivityLocationsBinding
import com.aab.locationreminder.utils.GeocoderHandler
import com.aab.locationreminder.utils.LocationHandler
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        val geocoderHandler = GeocoderHandler(this)
        locationHandler.getLocation { location ->
            CoroutineScope(Dispatchers.IO).launch {
                geocoderHandler.getAddressFromLocation(location){ address ->
                    Log.i(TAG, "Address -> $address")
                    address?.let { nullSafeAddress ->
                        Toast.makeText(
                            this@LocationsActivity,
                            nullSafeAddress.getAddressLine(1),
                            Toast.LENGTH_SHORT).show()
                        binding.mainText.text = "${nullSafeAddress.getAddressLine(0)}"
                    }
                }
            }
        }
    }
}