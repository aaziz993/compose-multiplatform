package gradle.plugins.doctor

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DoctorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply doctor properties.
            projectProperties.doctor?.applyTo()
        }
    }
}
