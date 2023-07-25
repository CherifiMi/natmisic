package com.example.natmisic.core.util

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun runTimePermission() {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    val permissionsToRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO,
                android.Manifest.permission.POST_NOTIFICATIONS,
            )
        }else {
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

    permissionsToRequest.forEach { permission ->
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                LocalContext.current,
                permission
            ) -> {
                // TODO:
            }
            else -> {
                SideEffect {
                    launcher.launch(permissionsToRequest)
                }
            }
        }
    }
}