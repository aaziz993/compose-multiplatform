package plugins.kotlin.rpc.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.rpc.RpcExtension
import gradle.plugins.kotlin.rpc.RpcStrictModeExtension
import gradle.project.EnabledSettings
import kotlinx.rpc.RpcDangerousApi
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RpcSettings(
    override val annotationTypeSafetyEnabled: Boolean? = null,
    override val strict: RpcStrictModeExtension? = null,
    override val enabled: Boolean = true,
) : RpcExtension, EnabledSettings {

    context(project: Project)
    @OptIn(RpcDangerousApi::class)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("kotlinx.rpc").id) {
            super.applyTo()
        }
}
