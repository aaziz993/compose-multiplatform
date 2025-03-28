package gradle.plugins.doctor.model

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.isCI
import gradle.api.services.unregister
import gradle.plugins.doctor.DoctorExtension
import gradle.plugins.doctor.JavaHomeHandler
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Suppress("PropertyName", "ktlint:standard:property-naming")
@Serializable
internal data class DoctorSettings(
    /** Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
     * See 'doctor.enableTaskMonitoring' in gradle.properties for details.
     */
    val enableTaskMonitoring: Boolean = true,
    override val disallowMultipleDaemons: Boolean? = null,
    override val downloadSpeedWarningThreshold: Float? = null,
    override val GCWarningThreshold: Float? = null,
    override val GCFailThreshold: Float? = null,
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
    override val enabled: Boolean = true,
) : DoctorExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("doctor").id) {
            super.applyTo()

            // Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
            // See 'doctor.enableTaskMonitoring' in gradle.properties for details.
            val enableTasksMonitoring = isCI || enableTaskMonitoring

            if (!enableTasksMonitoring) {
                logger.info("Gradle Doctor task monitoring is disabled.")
                gradle.sharedServices.unregister("listener-service")
            }
        }
}
