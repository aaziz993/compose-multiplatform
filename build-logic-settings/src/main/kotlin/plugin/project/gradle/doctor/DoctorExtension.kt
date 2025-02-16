package plugin.project.gradle.doctor

import com.osacky.doctor.DoctorExtension
import com.osacky.doctor.DoctorPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import gradle.amperModuleExtraProperties
import gradle.doctor
import gradle.isCI
import gradle.tryAssign
import gradle.unregister

internal fun Project.configureDoctorExtension() =
    plugins.withType<DoctorPlugin> {
        with(amperModuleExtraProperties.settings.gradle) {
            doctor {
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
    }

