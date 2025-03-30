package gradle.plugins.animalsniffer

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.animalsniffer.model.AnimalSnifferSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.animalSniffer?.takeIf{ pluginManager.hasPlugin("animalSniffer") }?.let { animalSniffer ->
                    plugins.apply(project.settings.libs.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
