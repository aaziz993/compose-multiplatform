package gradle.plugins.doctor

import gradle.accessors.projectProperties
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
