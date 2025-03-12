package plugin.project.kotlin.rpc

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RpcPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.rpc
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { rpc ->
                    plugins.apply(settings.libs.plugins.plugin("kotlinx.rpc").id)

                    rpc.applyTo()
                }
        }
    }
}
