package plugin.project.gradlle.doctor

import com.osacky.doctor.DoctorExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import gradle.amperModuleExtraProperties
import gradle.isCI
import gradle.unregister

internal fun Project.configureDoctorExtension(extension: DoctorExtension) =
    with(amperModuleExtraProperties.settings.gradle) {
        extension.apply {
            // Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
            // See 'doctor.enableTaskMonitoring' in gradle.properties for details.
            val enableTasksMonitoring = isCI || doctor.enableTaskMonitoring

            if (!enableTasksMonitoring) {
                logger.info("Gradle Doctor task monitoring is disabled.")
                project.gradle.sharedServices.unregister("listener-service")
            }

            doctor.enableTestCaching?.let(enableTestCaching::assign)

            // Disable JAVA_HOME validation as we use "Daemon JVM discovery" feature
            // https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria
            doctor.javaHome?.let { javaHome ->
                javaHome {
                    javaHome.ensureJavaHomeIsSet.let(ensureJavaHomeIsSet::assign)
                    javaHome.ensureJavaHomeMatches.let(ensureJavaHomeMatches::assign)
                }
            }
        }
    }

