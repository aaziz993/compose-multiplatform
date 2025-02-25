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
            projectProperties.plugins.kover
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { kover ->

                    plugins.apply(settings.libs.plugins.plugin("kover").id)

                    kover.applyTo()
                }
        }
    }
}
