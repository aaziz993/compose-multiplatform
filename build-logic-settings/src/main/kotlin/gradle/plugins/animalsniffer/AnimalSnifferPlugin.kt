package gradle.plugins.animalsniffer

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply animalsniffer properties.
            projectProperties.animalsniffer?.applyTo()
        }
    }
}
