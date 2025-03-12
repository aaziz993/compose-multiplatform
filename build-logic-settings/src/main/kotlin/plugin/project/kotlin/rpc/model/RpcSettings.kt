package plugin.project.kotlin.rpc.model

import gradle.id
import gradle.libs
import gradle.plugins.kotlin.rpc.RpcExtension
import gradle.plugins.kotlin.rpc.RpcStrictModeExtension
import gradle.plugins.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.rpc.RpcDangerousApi
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RpcSettings(
    override val annotationTypeSafetyEnabled: Boolean? = null,
    override val strict: RpcStrictModeExtension? = null,
    override val enabled: Boolean = true,
) : RpcExtension, EnabledSettings {

    context(Project)
    @OptIn(RpcDangerousApi::class)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlinx.rpc").id) {
            super.applyTo()
        }
}
