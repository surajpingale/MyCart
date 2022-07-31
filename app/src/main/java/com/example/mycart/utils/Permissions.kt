package com.example.mycart.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {

    private var permissions: Array<String> = emptyArray()


    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        this.permissions = permissions

        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun checkPermissions(activity: Activity): HashMap<String, Boolean> {
        val grantedPermissions: HashMap<String, Boolean> = HashMap()
        for (permission in permissions) {
            val granted = ContextCompat.checkSelfPermission(
                activity, permission
            ) == PackageManager.PERMISSION_GRANTED

            grantedPermissions[permission] = granted
        }
        return grantedPermissions
    }

    fun checkStoragePermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestStoragePermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            22
        )
    }
}