package plugin.project.gradle.doctor

import gradle.doctor
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureDoctorExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("doctor").id) {
       projectProperties.plugins.doctor.let { doctor ->
            doctor {
                doctor.applyTo(this)
            }
        }
    }

