package plugin.project.web.node

import gradle.amperModuleExtraProperties
import gradle.node
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

internal fun Project.configureNodeJsRootExtension() = plugins.withType<NodeJsPlugin> {
    amperModuleExtraProperties.settings.web.node.let { node ->
        node {
            ::downloadBaseUrl trySet node.downloadBaseUrl
            ::version trySet node.version
        }

        configureNodeJsEnvSpec()
    }
}
