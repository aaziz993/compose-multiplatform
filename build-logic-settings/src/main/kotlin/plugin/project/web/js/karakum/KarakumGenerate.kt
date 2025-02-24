package plugin.project.web.js.karakum

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKarakumGenerate() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
        projectProperties.karakum.task.let { task ->
            tasks.withType<KarakumGenerate> {
                task.applyTo(this)
            }
        }
    }
