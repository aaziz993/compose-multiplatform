package gradle.plugins.kotlin.rpc

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class RpcPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply kotlinx rpc properties.
            projectProperties.rpc?.applyTo()
        }
    }
}
