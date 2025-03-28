package gradle.plugins.doctor

import com.osacky.doctor.AppleRosettaTranslationCheckMode
import gradle.accessors.doctor
import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryApply
import gradle.api.tryAssign
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
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("doctor").id) {
            project.doctor.disallowMultipleDaemons tryAssign disallowMultipleDaemons
            project.doctor.downloadSpeedWarningThreshold tryAssign downloadSpeedWarningThreshold
            project.doctor.GCWarningThreshold tryAssign GCWarningThreshold
            project.doctor.GCFailThreshold tryAssign GCFailThreshold
            project.doctor.daggerThreshold tryAssign daggerThreshold
            project.doctor.enableTestCaching tryAssign enableTestCaching
            project.doctor.failOnEmptyDirectories tryAssign failOnEmptyDirectories
            project.doctor.allowBuildingAllAndroidAppsSimultaneously tryAssign allowBuildingAllAndroidAppsSimultaneously
            project.doctor.warnWhenJetifierEnabled tryAssign warnWhenJetifierEnabled
            project.doctor.negativeAvoidanceThreshold tryAssign negativeAvoidanceThreshold
            project.doctor.warnWhenNotUsingParallelGC tryAssign warnWhenNotUsingParallelGC
            project.doctor.disallowCleanTaskDependencies tryAssign disallowCleanTaskDependencies
            project.doctor.warnIfKotlinCompileDaemonFallback tryAssign warnIfKotlinCompileDaemonFallback
            project.doctor.appleRosettaTranslationCheckMode tryAssign appleRosettaTranslationCheckMode
            project.doctor::javaHome tryApply javaHome?.let{ javaHome -> javaHome::applyTo }
        }
}
