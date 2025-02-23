package plugin.project.gradle.kover

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.plugins.kover.enabled ||projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(settings.libs.plugins.plugin("kover").id)

            configureKoverExtension()
        }
    }
}
