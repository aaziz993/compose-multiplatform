package gradle.api.doctor

import com.osacky.doctor.DoctorExtension
import gradle.api.ci.CI
import gradle.api.services.unregister
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

// Always monitor tasks on CI, but disable it locally by default with providing an option to opt-in.
@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun DoctorExtension.taskMonitoring(): Unit =
    project.pluginManager.withPlugin("com.osacky.doctor") {
        if (CI != null) {
            project.logger.info("Gradle Doctor task monitoring is disabled.")
            project.gradle.sharedServices.unregister("listener-service")
        }
    }


public val Project.doctor: DoctorExtension get() = the()

public fun Project.doctor(configure: DoctorExtension.() -> Unit): Unit = extensions.configure(configure)
