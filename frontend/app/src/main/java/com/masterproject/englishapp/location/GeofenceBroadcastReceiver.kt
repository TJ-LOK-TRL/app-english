package com.masterproject.englishapp.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.masterproject.englishapp.navigation.deeplink.DeepLinkAction
import com.masterproject.englishapp.notification.AppNotificationManager

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GEOFENCE", "Receiver disparado!")
        val event = GeofencingEvent.fromIntent(intent) ?: return

        if (event.hasError()) {
            Log.e("GEOFENCE", "Erro no evento: ${event.errorCode}")
            return
        }

        // Verify if user entered the area
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("GEOFENCE", "Entrou na zona com sucesso!")
            val triggeredGeofences = event.triggeringGeofences
            triggeredGeofences?.forEach { geofence ->
                Log.d("GEOFENCE", "Entrou na zona: ${geofence.requestId}")
                AppNotificationManager.sendNotification(
                    context = context,
                    channelId = AppNotificationManager.CHANNEL_LOCATION,
                    notificationId = 1001,
                    title = "Welcome to the ${geofence.requestId}!",
                    message = "Ready to practice your English?",
                    command = "app://action/${DeepLinkAction.CONTEXTUAL_LESSON.routeDispatcher}?context=${geofence.requestId}"
                )
            }
        }
    }
}