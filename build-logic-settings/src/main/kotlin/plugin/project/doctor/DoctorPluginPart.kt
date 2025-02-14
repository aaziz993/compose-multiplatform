package plugin.project.doctor

import com.osacky.doctor.DoctorExtension
import plugin.utils.id
import plugin.utils.isCI
import plugin.utils.libs
import plugin.utils.plugin
import plugin.utils.plugins
import plugin.utils.settings
import plugin.utils.unregister
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.slf4j.LoggerFactory
import plugin.project.amperModuleExtraProperties

internal class DoctorPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {
    private val logger = LoggerFactory.getLogger(DoctorPluginPart::class.java)
    private val doctorRE get() = project.extensions.getByType<DoctorExtension>()

    private val doctor by lazy {
        project.amperModuleExtraProperties.settings?.doctor
    }

    override val needToApply: Boolean by lazy {
        doctor?.enabled == true && project == project.rootProject
    }

    override fun applyBeforeEvaluate() = with(project) {
        plugins.apply(project.settings.libs.plugins.plugin("doctor").id)
        applySettings()
    }

    fun applySettings() {
        val doctorSettings = doctor!!

        doctorRE.apply {
            // Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
            // See 'doctor.enableTaskMonitoring' in gradle.properties for details.
            val enableTasksMonitoring = isCI || doctorSettings.enableTaskMonitoring

            if (!enableTasksMonitoring) {
                logger.info("Gradle Doctor task monitoring is disabled.")
                project.gradle.sharedServices.unregister("listener-service")
            }
            enableTestCaching = doctorSettings.enableTestCaching

            // Disable JAVA_HOME validation as we use "Daemon JVM discovery" feature
            // https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria
            doctorSettings.javaHome?.let { javaHome ->
                javaHome {
                    javaHome.ensureJavaHomeIsSet.let { ensureJavaHomeIsSet = it }
                    javaHome.ensureJavaHomeMatches.let { ensureJavaHomeMatches = it }
                }
            }
        }
    }
}
