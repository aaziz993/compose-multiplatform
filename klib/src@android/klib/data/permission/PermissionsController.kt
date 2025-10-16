package klib.data.permission

import android.content.Context
import androidx.activity.ComponentActivity
import dev.icerock.moko.permissions.PermissionsController
import splitties.init.appCtx

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionsController(applicationContext: Context) :
    PermissionControllerImpl(PermissionsController(applicationContext)) {

    public fun bind(activity: ComponentActivity): Unit = permissionsController.bind(activity)
}
