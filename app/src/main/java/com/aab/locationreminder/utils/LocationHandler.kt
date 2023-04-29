package com.aab.locationreminder.utils

import android.Manifest
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices

class LocationHandler(context: Context) {

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var onPermissionGranted: (() -> Unit)? = null
    private var onPermissionDenied: (() -> Unit)? = null
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    fun registerRequest(activity: AppCompatActivity){
        locationPermissionRequest = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    onPermissionGranted?.let {
                        it()
                    }
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    onPermissionGranted?.let {
                        it()
                    }
                } else -> {
                    onPermissionDenied?.let {
                        it()
                    }
                }
            }
        }
    }

    fun requestForPermissions() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }


    fun setPermissionGranted(listener: ()->Unit){
        onPermissionGranted = listener
    }

    fun setPermissionDenied(listener: () -> Unit){
        onPermissionGranted = listener
    }


}