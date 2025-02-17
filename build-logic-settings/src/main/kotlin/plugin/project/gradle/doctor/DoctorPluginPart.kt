package plugin.project.gradle.doctor

import gradle.amperModuleExtraProperties
import gradle.isCI
import gradle.libs
import gradle.unregister
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class DoctorPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val doctor by lazy {
        project.amperModuleExtraProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.libs.plugins.doctor.get().pluginId)

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
