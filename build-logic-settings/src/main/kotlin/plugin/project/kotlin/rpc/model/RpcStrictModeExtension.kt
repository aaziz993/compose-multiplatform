package plugin.project.kotlin.rpc.model

import gradle.tryAssign
import kotlinx.rpc.RpcStrictMode
import kotlinx.rpc.RpcStrictModeExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class RpcStrictModeExtension(
    /**
     * `StateFlow`s in RPC services are deprecated,
     * due to their error-prone nature.
     *
     * Consider using plain flows and converting them to state on the application side.
     */
    val stateFlow: RpcStrictMode? = null,
    /**
     * `SharedFlow`s in RPC services are deprecated,
     * due to their error-prone nature.
     *
     * Consider using plain flows and converting them to state on the application side.
     */
    val sharedFlow: RpcStrictMode? = null,
    /**
     * Nested flows in RPC services are deprecated,
     * due to their error-prone nature.
     *
     * Consider using plain flows and converting them to state on the application side.
     */
    val nestedFlow: RpcStrictMode? = null,
    /**
     * Not top-level flows in the return value are deprecated in RPC for streaming.
     *
     * Consider returning a Flow and requesting other data in a different method.
     */
    val notTopLevelServerFlow: RpcStrictMode? = null,
    /**
     * Fields in RPC services are deprecated,
     * due to its error-prone nature.
     *
     * Consider using regular streaming.
     */
    val fields: RpcStrictMode? = null,
) {

    fun applyTo(extension: RpcStrictModeExtension) {
        extension.stateFlow tryAssign stateFlow
        extension.sharedFlow tryAssign sharedFlow
        extension.nestedFlow tryAssign nestedFlow
        extension.notTopLevelServerFlow tryAssign notTopLevelServerFlow
        extension.fields tryAssign fields
    }
}
