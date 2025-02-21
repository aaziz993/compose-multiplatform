package plugin.project.gradle.doctor.model

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import com.osacky.doctor.DoctorExtension
import gradle.isCI
import gradle.tryAssign
import gradle.unregister
import org.gradle.api.Project

internal interface DoctorExtension {

    /**
     * Throw an exception when multiple Gradle Daemons are running.
     */
    val disallowMultipleDaemons: Boolean?

    /**
     * Show a message if the download speed is less than this many megabytes / sec.
     */
    val downloadSpeedWarningThreshold: Float?

    /**
     * The level at which to warn when a build spends more than this percent garbage collecting.
     */
    val GCWarningThreshold: Float?

    /**
     * The level at which to fail when a build spends more than this percent garbage collecting.
     */
    val GCFailThreshold: Float?

    /**
     * Print a warning to the console if we spend more than this amount of time with Dagger annotation processors.
     */
    val daggerThreshold: Int?

    /**
     * By default, Gradle caches test results. This can be dangerous if tests rely on timestamps, dates, or other files
     * which are not declared as inputs.
     */
    val enableTestCaching: Boolean?

    /**
     * By default, Gradle treats empty directories as inputs to compilation tasks. This can cause cache misses.
     */
    val failOnEmptyDirectories: Boolean?

    /**
     * Do not allow building all apps simultaneously. This is likely not what the user intended.
     */
    val allowBuildingAllAndroidAppsSimultaneously: Boolean?

    /**
     * Warn if using Android Jetifier
     */
    val warnWhenJetifierEnabled: Boolean?

    /**
     * Negative Avoidance Savings Threshold
     * By default the Gradle Doctor will print out a warning when a task is slower to pull from the cache than to
     * re-execute. There is some variance in the amount of time a task can take when several tasks are running
     * concurrently. In order to account for this there is a threshold you can set. When the difference is above the
     * threshold, a warning is displayed.
     */
    val negativeAvoidanceThreshold: Int?

    /**
     * Warn when not using parallel GC.
     */
    val warnWhenNotUsingParallelGC: Boolean?

    /**
     * Throws an error when the `Delete` or `clean` task has dependencies.
     * If a clean task depends on other tasks, clean can be reordered and made to run after the tasks that would produce
     * output. This can lead to build failures or just strangeness with seemingly straightforward builds
     * (e.g., gradle clean build).
     * http://github.com/gradle/gradle/issues/2488
     */
    val disallowCleanTaskDependencies: Boolean?

    /**
     * Warn if using the Kotlin Compiler Daemon Fallback. The fallback is incredibly slow and should be avoided.
     * https://youtrack.jetbrains.com/issue/KT-48843
     */
    val warnIfKotlinCompileDaemonFallback: Boolean?

    /**
     * The mode in which the Apple Rosetta translation check is executed. Default is "ERROR".
     */
    val appleRosettaTranslationCheckMode: AppleRosettaTranslationCheckMode?

    /** Configures JAVA_HOME-specific behavior.
     */
    val javaHome: JavaHomeHandler?

    context(Project)
    fun applyTo(extension: DoctorExtension) {
        extension.disallowMultipleDaemons tryAssign disallowMultipleDaemons
        extension.downloadSpeedWarningThreshold tryAssign downloadSpeedWarningThreshold
        extension.GCWarningThreshold tryAssign GCWarningThreshold
        extension.GCFailThreshold tryAssign GCFailThreshold
        extension.daggerThreshold tryAssign daggerThreshold
        extension.enableTestCaching tryAssign enableTestCaching
        extension.failOnEmptyDirectories tryAssign failOnEmptyDirectories
        extension.allowBuildingAllAndroidAppsSimultaneously tryAssign allowBuildingAllAndroidAppsSimultaneously
        extension.warnWhenJetifierEnabled tryAssign warnWhenJetifierEnabled
        extension.negativeAvoidanceThreshold tryAssign negativeAvoidanceThreshold
        extension.warnWhenNotUsingParallelGC tryAssign warnWhenNotUsingParallelGC
        extension.disallowCleanTaskDependencies tryAssign disallowCleanTaskDependencies
        extension.warnIfKotlinCompileDaemonFallback tryAssign warnIfKotlinCompileDaemonFallback
        extension.appleRosettaTranslationCheckMode tryAssign appleRosettaTranslationCheckMode

        javaHome?.let { javaHome ->
            extension.javaHome (javaHome::applyTo)
        }
    }
}
