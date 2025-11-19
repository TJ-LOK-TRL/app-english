package com.masterproject.englishapp.permissions

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: ComponentActivity) {

    private val grantedMap = mutableMapOf<AppPermission, Boolean>()

    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach { (name, granted) ->
                val appPerm = AppPermission.fromSystemName(name)
                if (appPerm != null) {
                    grantedMap[appPerm] = granted
                    Log.d("PermissionManager", "${appPerm.name} -> $granted")

                    if (!granted) {
                        if (activity.shouldShowRequestPermissionRationale(name)) {
                            showRationale(appPerm)
                        } else {
                            showPermanentDenial(appPerm)
                        }
                    }
                }
            }
        }

    /** Request one or more permissions */
    fun request(vararg permissions: AppPermission) {
        val missing = permissions.filterNot { isGranted(it) }

        if (missing.isEmpty()) {
            Log.d("PermissionManager", "All permissions already granted")
            return
        }

        val needingRationale = missing.filter {
            activity.shouldShowRequestPermissionRationale(it.systemName)
        }

        if (needingRationale.isNotEmpty()) {
            showRationaleDialog(needingRationale) {
                launcher.launch(missing.map { it.systemName }.toTypedArray())
            }
        } else {
            launcher.launch(missing.map { it.systemName }.toTypedArray())
        }
    }

    /** Check if permission is granted */
    fun isGranted(permission: AppPermission): Boolean {
        return grantedMap[permission] ?: run {
            val granted = ContextCompat.checkSelfPermission(
                activity,
                permission.systemName
            ) == PackageManager.PERMISSION_GRANTED
            grantedMap[permission] = granted
            granted
        }
    }

    /** Check if all listed permissions are granted */
    fun areAllGranted(vararg permissions: AppPermission): Boolean =
        permissions.all { isGranted(it) }

    fun ensurePermissions(
        required: List<AppPermission>,
        onGranted: () -> Unit,
        onDenied: (() -> Unit)? = null
    ) {
        if (required.isEmpty()) {
            onGranted()
            return
        }

        val allGranted = areAllGranted(*required.toTypedArray())
        if (allGranted) {
            onGranted()
        } else {
            request(*required.toTypedArray())

            val postRequestAllGranted = areAllGranted(*required.toTypedArray())
            if (postRequestAllGranted) onGranted() else onDenied?.invoke()
        }
    }

    private fun showRationale(permission: AppPermission) {
        Toast.makeText(activity, permission.rationale, Toast.LENGTH_LONG).show()
    }

    private fun showRationaleDialog(
        permissions: List<AppPermission>,
        onContinue: () -> Unit
    ) {
        val message = permissions.joinToString("\n\n") { it.rationale }

        AlertDialog.Builder(activity)
            .setTitle("Permission Needed")
            .setMessage(message)
            .setPositiveButton("Continue") { _, _ -> onContinue() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPermanentDenial(permission: AppPermission) {
        Toast.makeText(
            activity,
            "Permission permanently denied: ${permission.name}. Please enable it in app settings.",
            Toast.LENGTH_LONG
        ).show()
    }
}
