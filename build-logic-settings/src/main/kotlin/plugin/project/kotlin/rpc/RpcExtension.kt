package plugin.project.kotlin.rpc

import gradle.moduleProperties
import gradle.rpc
import gradle.tryAssign
import kotlinx.rpc.RpcDangerousApi
import kotlinx.rpc.RpcGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@OptIn(RpcDangerousApi::class)
internal fun Project.configureRpcExtension() =
    plugins.withType<RpcGradlePlugin> {
        moduleProperties.settings.kotlin.rpc.let { rpc ->
            rpc {
                annotationTypeSafetyEnabled tryAssign rpc.annotationTypeSafetyEnabled
                rpc.strict?.let { strict ->
                    strict {
                        stateFlow tryAssign strict.stateFlow
                        sharedFlow tryAssign strict.sharedFlow
                        nestedFlow tryAssign strict.nestedFlow
                        notTopLevelServerFlow tryAssign strict.notTopLevelServerFlow
                        fields tryAssign strict.fields
                    }
                }
            }
        }
    }
