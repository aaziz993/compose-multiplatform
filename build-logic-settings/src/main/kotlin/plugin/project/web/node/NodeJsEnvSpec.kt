package plugin.project.web.node

import gradle.amperModuleExtraProperties
import gradle.nodeEnv
import gradle.tryAssign
import org.gradle.api.Project

internal fun Project.configureNodeJsEnvSpec() =
    amperModuleExtraProperties.settings.web.node.env.let { env ->
        nodeEnv {
            download tryAssign env.download
            downloadBaseUrl tryAssign env.downloadBaseUrl
            installationDirectory tryAssign env.installationDirectory?.let(layout.projectDirectory::dir)
            version tryAssign env.version
            command tryAssign env.command
        }
    }
