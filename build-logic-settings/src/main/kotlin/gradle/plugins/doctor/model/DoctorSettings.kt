package gradle.plugins.doctor.model

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import gradle.api.ci.CI
import gradle.api.services.unregister
import gradle.plugins.doctor.DoctorExtension
import gradle.plugins.doctor.JavaHomeHandler
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Suppress("PropertyName", "ktlint:standard:property-naming")
@Serializable
internal data class DoctorSettings(
    override val disallowMultipleDaemons: Boolean? = null,
    override val downloadSpeedWarningThreshold: Float? = null,
    override val gcWarningThreshold: Float? = null,
    override val gcFailThreshold: Float? = null,
    override val daggerThreshold: Int? = null,
    override val enableTestCaching: Boolean? = null,
    override val failOnEmptyDirectories: Boolean? = null,
    override val allowBuildingAllAndroidAppsSimultaneously: Boolean? = null,
    override val warnWhenJetifierEnabled: Boolean? = null,
    override val negativeAvoidanceThreshold: Int? = null,
    override val warnWhenNotUsingParallelGC: Boolean? = null,
    override val disallowCleanTaskDependencies: Boolean? = null,
    override val warnIfKotlinCompileDaemonFallback: Boolean? = null,
    override val appleRosettaTranslationCheckMode: AppleRosettaTranslationCheckMode? = null,
    override val javaHome: JavaHomeHandler? = null,
    /** Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
     * See 'doctor.enableTaskMonitoring' in gradle.properties for details.
     */
    val enableTaskMonitoring: Boolean = true,
) : DoctorExtension {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin("com.osacky.doctor") {
            super.applyTo()

            // Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
            // See 'doctor.enableTaskMonitoring' in gradle.properties for details.
            val enableTasksMonitoring = CI.present || enableTaskMonitoring

            if (!enableTasksMonitoring) {
                logger.info("Gradle Doctor task monitoring is disabled.")
                gradle.sharedServices.unregister("listener-service")
            }
        }
}
