package plugin.project.kotlin.rpc

import gradle.projectProperties
import gradle.rpc
import gradle.settings
import kotlinx.rpc.RpcDangerousApi
import kotlinx.rpc.RpcGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@OptIn(RpcDangerousApi::class)
internal fun Project.configureRpcExtension() =
    plugins.withType<RpcGradlePlugin> {
       projectProperties.plugins.rpc.let { rpc ->
            rpc(rpc::applyTo)
        }
    }
