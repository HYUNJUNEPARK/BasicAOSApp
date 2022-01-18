package com.june.pictureframe

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/*
HOW TO USE
1. 사용하려는 Activity 에 Permission 상속
2. Permission 을 상속 받은 Activity 에 override fun permissionGranted / override fun permissionDenied
3. requestPermissions(permissions, requestCode) 을 호출해 사용
*/

abstract class Permission : AppCompatActivity() {
    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted = permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }
}