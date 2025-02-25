package plugin.project.kotlin.allopen

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AllOpenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.allOpen
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { allOpen ->
                    plugins.apply(settings.libs.plugins.plugin("allopen").id)

                    allOpen.applyTo()
                }
        }
    }
}
