package plugin.project.kotlin.rpc

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.rpc
import gradle.settings
import kotlinx.rpc.RpcDangerousApi
import kotlinx.rpc.RpcGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@OptIn(RpcDangerousApi::class)
internal fun Project.configureRpcExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("kotlinx.rpc").id) {
       projectProperties.plugins.rpc.let { rpc ->
            rpc(rpc::applyTo)
        }
    }
