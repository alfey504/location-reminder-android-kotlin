package com.aab.locationreminder.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aab.locationreminder.utils.NotificationHandler

class GeofenceBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("GeofenceBroadCastReceiver", "Inside on receive")
        context?.let { context ->
            val notificationHandler = NotificationHandler(context)
            notificationHandler.displaySimpleNotification("Morrish", "In range")
        }
    }
}