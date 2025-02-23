package plugin.project.gradle.githooks

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.gitHooks

internal fun Settings.configureGitHooksExtension() =
    pluginManager.withPlugin(libs.plugins.plugin("gradle-pre-commit-git-hooks").id) {
       projectProperties.plugins.gitHooks.let { gitHooks ->
            gitHooks {
                gitHooks.applyTo(this)
            }
        }
    }
