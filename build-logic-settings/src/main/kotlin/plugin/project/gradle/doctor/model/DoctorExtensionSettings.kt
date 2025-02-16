package plugin.project.gradle.doctor.model

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import kotlinx.serialization.Serializable

@Suppress("PropertyName","ktlint:standard:property-naming")
@Serializable
internal data class DoctorExtensionSettings(
    val enabled: Boolean = true,
    /** Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
     * See 'doctor.enableTaskMonitoring' in gradle.properties for details.
     */
    val enableTaskMonitoring: Boolean = true,
    /**
     * Throw an exception when multiple Gradle Daemons are running.
     */
    val disallowMultipleDaemons: Boolean? = null,
    /**
     * Show a message if the download speed is less than this many megabytes / sec.
     */
    val downloadSpeedWarningThreshold: Float? = null,
    /**
     * The level at which to warn when a build spends more than this percent garbage collecting.
     */
    val GCWarningThreshold: Float? = null,
    /**
     * The level at which to fail when a build spends more than this percent garbage collecting.
     */
    val GCFailThreshold: Float? = null,
    /**
     * Print a warning to the console if we spend more than this amount of time with Dagger annotation processors.
     */
    val daggerThreshold: Int? = null,
    /**
     * By default, Gradle caches test results. This can be dangerous if tests rely on timestamps, dates, or other files
     * which are not declared as inputs.
     */
    val enableTestCaching: Boolean? = null,

    /**
     * By default, Gradle treats empty directories as inputs to compilation tasks. This can cause cache misses.
     */
    val failOnEmptyDirectories: Boolean? = null,

    /**
     * Do not allow building all apps simultaneously. This is likely not what the user intended.
     */
    val allowBuildingAllAndroidAppsSimultaneously: Boolean? = null,

    /**
     * Warn if using Android Jetifier
     */
    val warnWhenJetifierEnabled: Boolean? = null,
    /**
     * Negative Avoidance Savings Threshold
     * By default the Gradle Doctor will print out a warning when a task is slower to pull from the cache than to
     * re-execute. There is some variance in the amount of time a task can take when several tasks are running
     * concurrently. In order to account for this there is a threshold you can set. When the difference is above the
     * threshold, a warning is displayed.
     */
    val negativeAvoidanceThreshold: Int? = null,
    /**
     * Warn when not using parallel GC.
     */
    val warnWhenNotUsingParallelGC: Boolean? = null,
    /**
     * Throws an error when the `Delete` or `clean` task has dependencies.
     * If a clean task depends on other tasks, clean can be reordered and made to run after the tasks that would produce
     * output. This can lead to build failures or just strangeness with seemingly straightforward builds
     * (e.g., gradle clean build).
     * http://github.com/gradle/gradle/issues/2488
     */
    val disallowCleanTaskDependencies: Boolean? = null,
    /**
     * Warn if using the Kotlin Compiler Daemon Fallback. The fallback is incredibly slow and should be avoided.
     * https://youtrack.jetbrains.com/issue/KT-48843
     */
    val warnIfKotlinCompileDaemonFallback: Boolean? = null,
    /**
     * The mode in which the Apple Rosetta translation check is executed. Default is "ERROR".
     */
    val appleRosettaTranslationCheckMode: AppleRosettaTranslationCheckMode? = null,
    /** Configures JAVA_HOME-specific behavior.
     */
    val javaHome: JavaHomeHandler? = null,
)
