package com.aab.locationreminder.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aab.locationreminder.activities.LocationsActivity
import com.aab.locationreminder.activities.ReminderActivity

class LocationCheckBroadcastReceiver: BroadcastReceiver() {

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"

        const val ACTION_LOCATION_UPDATE = "location_update"
    }

    private lateinit var context: Context
    private var onLocationReceived: ((location: Location) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { this.context = it }
        intent?.let { safeIntent -> parseLocationFromIntent(safeIntent) }
    }

    private fun parseLocationFromIntent(intent: Intent){
        val extras = intent.extras
        extras?.let{
            val location = Location("")
            it.getString(LATITUDE)?.let { lat -> location.latitude = lat.toDouble() }
            it.getString(LONGITUDE)?.let { lng -> location.longitude = lng.toDouble() }
            onLocationReceived?.let { listener ->
                Log.i("LocationCheckBroadcastReceiver", "parseLocationFromIntent $location")
                listener(location)
            }
        }
    }

    fun setOnLocationUpdate(listener: (location: Location) -> Unit){
        onLocationReceived = listener
    }

}