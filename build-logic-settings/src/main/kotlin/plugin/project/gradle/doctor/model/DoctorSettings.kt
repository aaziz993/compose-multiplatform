package plugin.project.gradle.doctor.model

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import kotlinx.serialization.Serializable

@Suppress("PropertyName", "ktlint:standard:property-naming")
@Serializable
internal data class DoctorSettings(
    val enabled: Boolean = true,
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
) : DoctorExtension
