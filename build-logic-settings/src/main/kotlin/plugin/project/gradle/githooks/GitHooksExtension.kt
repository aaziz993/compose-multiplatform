package plugin.project.gradle.githooks

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.trySet
import java.net.URI
import org.danilopianini.gradle.git.hooks.GradleGitHooksPlugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.gitHooks
import org.gradle.kotlin.dsl.withType

internal fun Settings.configureGitHooksExtension() =
    pluginManager.withPlugin(libs.plugins.plugin("gradle-pre-commit-git-hooks").id) {
       settings.projectProperties.plugins.gitHooks.let { gitHooks ->
            gitHooks {
                gitHooks.applyTo(this)
            }
        }
    }
