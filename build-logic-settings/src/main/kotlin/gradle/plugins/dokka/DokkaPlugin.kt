package gradle.plugins.dokka

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.dokka.model.DokkaSettings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply dokka properties.
            projectProperties.dokka?.applyTo()
        }
    }
}
