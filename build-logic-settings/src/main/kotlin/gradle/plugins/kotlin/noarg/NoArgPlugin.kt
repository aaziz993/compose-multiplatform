package gradle.plugins.kotlin.noarg

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.noarg.model.NoArgSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.noArg
                .takeIf(NoArgSettings::enabled)?.let { noArg ->
                    plugins.apply(project.settings.libs.plugins.plugin("allopen").id)

                    noArg.applyTo()
                }
        }
    }
}
