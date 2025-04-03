package gradle.plugins.animalsniffer

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply animalSniffer properties.
            projectProperties.animalSniffer?.applyTo()
        }
    }
}
