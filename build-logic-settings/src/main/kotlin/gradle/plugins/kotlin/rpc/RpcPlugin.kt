package gradle.plugins.kotlin.rpc


import gradle.accessors.catalog.libs


import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.rpc.model.RpcSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RpcPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.rpc
                .takeIf(RpcSettings::enabled)?.let { rpc ->
                    plugins.apply(project.settings.libs.plugin("kotlinx.rpc").id)

                    rpc.applyTo()
                }
        }
    }
}
