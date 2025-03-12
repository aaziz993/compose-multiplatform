package plugin.project.gradle.animalsniffer

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.animalSniffer
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { animalSniffer ->
                    plugins.apply(settings.libs.plugins.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
