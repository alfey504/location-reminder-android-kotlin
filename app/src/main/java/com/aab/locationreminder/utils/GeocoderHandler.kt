package com.aab.locationreminder.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeocoderHandler(context: Context) {
    companion object{
        const val TAG = "GeocoderHandler"
    }

    private val mGeocoder = Geocoder(context)

    suspend fun getAddressFromLocation(
        location: Location,
        onAddress: (address: Address?) -> Unit
    ){
        withContext(Dispatchers.IO){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mGeocoder.getFromLocation(location.latitude, location.longitude, 1) {
                    CoroutineScope(Dispatchers.Main).launch {
                        onAddress(it.firstOrNull())
                    }
                }
                return@withContext
            }

            try {
                val address = mGeocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                CoroutineScope(Dispatchers.Main).launch {
                    onAddress(address)
                    Log.i(TAG, "Address fetched")
                }
            } catch(e: Exception) {
                Log.i(TAG, e.toString())
                onAddress(null)
            }
        }
    }
}