package plugin.project.kotlin.rpc.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.rpc
import gradle.settings
import gradle.tryAssign
import kotlinx.rpc.RpcDangerousApi
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

@Serializable
internal data class RpcSettings(
    override val annotationTypeSafetyEnabled: Boolean? = null,
    override val strict: RpcStrictModeExtension? = null,
    override val enabled: Boolean = true,
) : RpcExtension, EnabledSettings {

    context(Project)
    @OptIn(RpcDangerousApi::class)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("kotlinx.rpc").id) {
            rpc.annotationTypeSafetyEnabled tryAssign annotationTypeSafetyEnabled

            strict?.let { strict ->
                rpc.strict(strict::applyTo)
            }
        }
}
