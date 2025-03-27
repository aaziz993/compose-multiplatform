package gradle.plugins.doctor

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.doctor.model.DoctorSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DoctorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.doctor.takeIf(DoctorSettings::enabled)?.let { doctor ->
                plugins.apply(project.settings.libs.plugins.plugin("doctor").id)

                doctor.applyTo()
            }
        }
    }
}
