package permission

import android.content.Context
import androidx.activity.ComponentActivity
import dev.icerock.moko.permissions.PermissionsController

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController(applicationContext: Context) :
    PermissionControllerImpl(PermissionsController(applicationContext)) {
    public fun bind(activity: ComponentActivity) {
        permissionsController.bind(activity)
    }
}
