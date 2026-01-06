package com.masterproject.englishapp.location

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.masterproject.englishapp.permissions.AppPermission
import com.masterproject.englishapp.permissions.PermissionManager
import com.masterproject.englishapp.user.UserPreferencesStore

@Composable
fun LocationContextHandler(
    permissionManager: PermissionManager,
    userPreferencesStore: UserPreferencesStore
) {
    val context = LocalContext.current
    val geofenceManager = remember { GeofenceManager(context) }
    val gpsEnabled = userPreferencesStore.gpsNotificationsEnabled

    LaunchedEffect(gpsEnabled) {
        if (gpsEnabled) {
            permissionManager.ensurePermissions(
                listOf(AppPermission.LOCATION_FINE, AppPermission.LOCATION_COARSE),
                onGranted = {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        if (!permissionManager.isGranted(AppPermission.LOCATION_BACKGROUND)) {
                            Log.d("GEOFENCE", "No location background location permission granted, requesting now...")
                            permissionManager.request(AppPermission.LOCATION_BACKGROUND)
                        }
                    }

                    // Activate here
                    geofenceManager.addGeofence("AIRPORT_ZONE", 38.7742, -9.1342)
                    geofenceManager.addGeofence("MY_HOME", 39.66951297788203, -9.008010017927896)
                }
            )
        } else {
            geofenceManager.removeGeofences()
        }
    }
}