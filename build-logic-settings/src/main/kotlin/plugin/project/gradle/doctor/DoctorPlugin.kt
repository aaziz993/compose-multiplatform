package plugin.project.gradle.doctor

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DoctorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.doctor.takeIf { it.enabled && project == rootProject }?.let { doctor ->
                plugins.apply(settings.libs.plugins.plugin("doctor").id)

                doctor.applyTo()
            }
        }
    }
}
