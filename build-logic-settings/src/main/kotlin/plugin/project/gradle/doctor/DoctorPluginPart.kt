package plugin.project.gradle.doctor

import gradle.id
import gradle.isCI
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.unregister
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DoctorPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        if (!projectProperties.plugins.doctor.enabled || this != rootProject) {
            return@with
        }

        plugins.apply(settings.libs.plugins.plugin("doctor").id)

        configureDoctorExtension()

        // Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
        // See 'doctor.enableTaskMonitoring' in gradle.properties for details.
        val enableTasksMonitoring = isCI || providers.gradleProperty("doctor.enable-task-monitoring").get().toBoolean()

        if (!enableTasksMonitoring) {
            logger.info("Gradle Doctor task monitoring is disabled.")
            gradle.sharedServices.unregister("listener-service")
        }
    }
}
