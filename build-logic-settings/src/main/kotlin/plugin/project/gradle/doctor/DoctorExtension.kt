package plugin.project.gradle.doctor

import gradle.doctor
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureDoctorExtension() =
    pluginManager.withPlugin(libs.plugins.doctor.get().pluginId) {
       settings.projectProperties.plugins.doctor.let { doctor ->
            doctor {
                doctor.applyTo(this)
            }
        }
    }

