package plugin.project.kotlin.rpc.model

import kotlinx.serialization.Serializable

@Serializable
internal data class RpcSettings(
    override val annotationTypeSafetyEnabled: Boolean? = null,
    override val strict: RpcStrictModeExtension? = null,
    val enabled: Boolean = true,
) : RpcExtension
