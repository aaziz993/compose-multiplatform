package plugin.project.gradle.doctor

import gradle.moduleProperties
import gradle.isCI
import gradle.libs
import gradle.unregister
import org.gradle.api.Project
import plugin.project.BindingPluginPart

internal class DoctorPluginPart(override val project: Project) : BindingPluginPart {

    private val doctor by lazy {
        project.moduleProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.doctor.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
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
