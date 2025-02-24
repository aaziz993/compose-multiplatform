package plugin.project.gradle.doctor

import com.android.tools.r8.internal.dO
import gradle.id
import gradle.isCI
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.unregister
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DoctorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.doctor.takeIf { it.enabled && project == rootProject }?.let { doctor ->
                plugins.apply(settings.libs.plugins.plugin("doctor").id)

                doctor.applyTo()
            }
        }
    }
}
