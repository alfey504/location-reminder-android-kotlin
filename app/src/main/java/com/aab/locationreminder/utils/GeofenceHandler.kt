package com.aab.locationreminder.utils

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

data class GeofenceElement (
        val id: String,
        val location: Location,
        val radius: Float)

class GeofenceHandlerBuilder(context: Context) {
    private val geofenceHandler: GeofenceHandler = GeofenceHandler(context)
    private var pendingIntent: PendingIntent? = null

    fun addGeofenceList(geofenceElements: List<GeofenceElement>): GeofenceHandlerBuilder{
        geofenceHandler.makeGeofenceList(geofenceElements)
        return this
    }

    fun addPendingIntent(pendingIntent: PendingIntent): GeofenceHandlerBuilder{
        this.pendingIntent = pendingIntent
        return this
    }

    fun addOnSuccessListener(listener: () -> Unit){
        geofenceHandler.addOnSuccessListener(listener)
    }

    fun addOnFailureListener(listener: () -> Unit){
        geofenceHandler.addOnFailureListener(listener)
    }

    fun start(): GeofenceHandler{
        pendingIntent?.let { pendingIntent ->
            geofenceHandler.startClient(pendingIntent)
        } ?: run {
            throw Exception("Pending Intent Not added")
        }
        return geofenceHandler
    }
}

class GeofenceHandler(context: Context) {

    private val geofencingClient = LocationServices.getGeofencingClient(context)
    private val geofenceList: MutableList<Geofence> = mutableListOf()
    private val locationHandler: LocationHandler = LocationHandler(context)
    private var geofenceAddOnSuccessListener: (() -> Unit) ?= null
    private var geofenceAddOnFailureListener: (() -> Unit) ?= null

    fun makeGeofenceList(geofenceElements: List<GeofenceElement>){
        for (geofenceElement in geofenceElements){
            Log.i("GeofenceHandler", "Adding geofence ${geofenceElement.id}")
            geofenceList.add(buildGeofence(geofenceElement))
        }
    }

    private fun buildGeofence(geofenceElement: GeofenceElement): Geofence{
        return Geofence.Builder()
            .setRequestId(geofenceElement.id)
            .setCircularRegion(
                geofenceElement.location.latitude,
                geofenceElement.location.longitude,
                geofenceElement.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    }

    private fun geofencingRequest(geofenceList: List<Geofence>):  GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    fun startClient(pendingIntent: PendingIntent){
        Log.i("GeofenceHandler", "Starting Service")
        if(locationHandler.checkPermission()){
            geofencingClient.addGeofences(geofencingRequest(geofenceList.toList()), pendingIntent).run {
                addOnSuccessListener {
                    Log.i("GeofenceHandler", "Successfully Started Service")
                    geofenceAddOnSuccessListener?.let { it() }
                }
                addOnFailureListener {
                    Log.i("GeofenceHandler", "Exception:$it")
                    geofenceAddOnFailureListener?.let { it() }
                }
            }
        }
    }

    fun addOnSuccessListener(listener: ()-> Unit){
        geofenceAddOnSuccessListener = listener
    }

    fun addOnFailureListener(listener: () -> Unit){
        geofenceAddOnFailureListener = listener
    }

}