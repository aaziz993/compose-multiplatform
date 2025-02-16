package plugin.project.gradle.doctor

import com.osacky.doctor.DoctorExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import gradle.amperModuleExtraProperties
import gradle.id
import gradle.isCI
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.unregister

internal class DoctorPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val doctor by lazy {
        project.amperModuleExtraProperties.settings.gradle.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor.enabled && project == project.rootProject
    }

    override fun applyBeforeEvaluate() {
        super.applyBeforeEvaluate()

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
