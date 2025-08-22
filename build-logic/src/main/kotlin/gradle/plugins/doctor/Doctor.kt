package gradle.plugins.doctor

import com.osacky.doctor.DoctorExtension
import gradle.api.ci.CI
import gradle.api.services.unregister
import org.gradle.api.Project

// Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
context(project: Project)
public fun DoctorExtension.disableTaskMonitoring(): Unit =
    project.pluginManager.withPlugin("com.osacky.doctor") {
        if (CI.current != null) {
            project.logger.info("Gradle Doctor task monitoring is disabled.")
            project.gradle.sharedServices.unregister("listener-service")
        }
    }
