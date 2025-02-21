package plugin.project.gradle.doctor

import com.osacky.doctor.DoctorPlugin
import gradle.doctor
import gradle.isCI
import gradle.libs
import gradle.moduleProperties
import gradle.tryAssign
import gradle.unregister
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDoctorExtension() =
    pluginManager.withPlugin(libs.plugins.doctor.get().pluginId) {
        moduleProperties.settings.gradle.doctor.let { doctor ->
            doctor {
                doctor.applyTo(this)
            }
        }
    }

