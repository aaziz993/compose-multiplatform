package plugin.project.web.node

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.nodeEnv
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

internal fun Project.configureNodeJsEnvSpec() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
       projectProperties.settings.web.nodeJsEnv.let { env ->
            nodeEnv {
                download tryAssign env.download
                downloadBaseUrl tryAssign env.downloadBaseUrl
                installationDirectory tryAssign env.installationDirectory?.let(layout.projectDirectory::dir)
                version tryAssign env.version
                command tryAssign env.command
            }
        }
    }
