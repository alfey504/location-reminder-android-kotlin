package com.aab.locationreminder.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class LocationHandler(private val context: Context) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
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
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    onPermissionGranted?.let {
                        it()
                    }
                }else -> {
                    onPermissionDenied?.let {
                        it()
                    }
                }
            }
        }
    }

    fun requestForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ))
        }else{
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    fun checkPermission(): Boolean{
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
//    fun getLocation(onComplete: (location: Location) -> Unit){
//        if(!checkPermission()) return
//        fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task->
//            task.result?.let {
//                onComplete(it)
//            }
//        }
//    }

    fun getLocation(onComplete: (location: Location) -> Unit){
        if(!checkPermission()) return
        val locationFinder = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
        locationFinder.addOnCompleteListener{ task->
            task.result?.let {
                onComplete(it)
            }
        }
    }

    fun setPermissionGranted(listener: ()->Unit){
        onPermissionGranted = listener
    }

    fun setPermissionDenied(listener: () -> Unit){
        onPermissionGranted = listener
    }

    fun isWithinRange(location1: Location, location2: Location): Boolean{
        val distance = location1.distanceTo(location2)
        return distance < 350
    }


}