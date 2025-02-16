package plugin.project.gradle.doctor

import com.osacky.doctor.DoctorExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import gradle.amperModuleExtraProperties
import gradle.isCI
import gradle.tryAssign
import gradle.unregister

internal fun Project.configureDoctorExtension(extension: DoctorExtension) =
    with(amperModuleExtraProperties.settings.gradle) {
        extension.apply {
            val enableTasksMonitoring = isCI || doctor.enableTaskMonitoring

            if (!enableTasksMonitoring) {
                logger.info("Gradle Doctor task monitoring is disabled.")
                project.gradle.sharedServices.unregister("listener-service")
            }


            disallowMultipleDaemons tryAssign doctor.disallowMultipleDaemons
            downloadSpeedWarningThreshold tryAssign doctor.downloadSpeedWarningThreshold
            GCWarningThreshold tryAssign doctor.GCWarningThreshold
            GCFailThreshold tryAssign doctor.GCFailThreshold
            daggerThreshold tryAssign doctor.daggerThreshold
            enableTestCaching tryAssign doctor.enableTestCaching
            failOnEmptyDirectories tryAssign doctor.failOnEmptyDirectories
            allowBuildingAllAndroidAppsSimultaneously tryAssign doctor.allowBuildingAllAndroidAppsSimultaneously
            warnWhenJetifierEnabled tryAssign doctor.warnWhenJetifierEnabled
            negativeAvoidanceThreshold tryAssign doctor.negativeAvoidanceThreshold
            warnWhenNotUsingParallelGC tryAssign doctor.warnWhenNotUsingParallelGC
            disallowCleanTaskDependencies tryAssign doctor.disallowCleanTaskDependencies
            warnIfKotlinCompileDaemonFallback tryAssign doctor.warnIfKotlinCompileDaemonFallback
            appleRosettaTranslationCheckMode tryAssign doctor.appleRosettaTranslationCheckMode

            doctor.javaHome?.let { javaHome ->
                javaHome {
                    ensureJavaHomeMatches tryAssign javaHome.ensureJavaHomeMatches
                    ensureJavaHomeIsSet tryAssign javaHome.ensureJavaHomeIsSet
                    failOnError tryAssign javaHome.failOnError
                    extraMessage tryAssign javaHome.extraMessage
                }
            }
        }
    }

