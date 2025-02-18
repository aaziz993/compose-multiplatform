package plugin.project.kotlin.rpc

import gradle.amperModuleExtraProperties
import gradle.libs
import gradle.rpc
import gradle.tryAssign
import kotlinx.rpc.RpcDangerousApi
import kotlinx.rpc.RpcExtension
import kotlinx.rpc.RpcGradlePlugin
import kotlinx.rpc.RpcStrictMode
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import plugin.project.kotlin.rpc.model.RpcStrictModeExtension

@OptIn(RpcDangerousApi::class)
internal fun Project.configureRpcExtension() =
    plugins.withType<RpcGradlePlugin> {
        amperModuleExtraProperties.settings.kotlin.rpc.let { rpc ->
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
