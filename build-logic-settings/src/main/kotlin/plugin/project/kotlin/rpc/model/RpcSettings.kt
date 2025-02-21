package plugin.project.kotlin.rpc.model

import gradle.tryAssign
import kotlinx.rpc.RpcDangerousApi
import kotlinx.serialization.Serializable

@Serializable
internal data class RpcSettings(
    override val annotationTypeSafetyEnabled: Boolean? = null,
    override val strict: RpcStrictModeExtension? = null,
    val enabled: Boolean = true,
) : RpcExtension {

    @OptIn(RpcDangerousApi::class)
    fun applyTo(extension: kotlinx.rpc.RpcExtension) {
        extension.annotationTypeSafetyEnabled tryAssign annotationTypeSafetyEnabled
        strict?.let { strict ->
            extension.strict(strict::applyTo)
        }
    }
}
