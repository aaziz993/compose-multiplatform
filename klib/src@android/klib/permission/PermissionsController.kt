package klib.permission

import android.content.Context
import androidx.activity.ComponentActivity
import dev.icerock.moko.permissions.PermissionsController

public actual class PermissionsController(applicationContext: Context) :
    PermissionControllerImpl(PermissionsController(applicationContext)) {

    public fun bind(activity: ComponentActivity): Unit = permissionsController.bind(activity)
}
