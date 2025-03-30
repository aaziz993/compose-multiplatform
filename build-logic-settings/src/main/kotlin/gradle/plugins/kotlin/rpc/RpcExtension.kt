package gradle.plugins.kotlin.rpc

import gradle.accessors.rpc
import gradle.api.tryAssign
import kotlinx.rpc.RpcDangerousApi
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class RpcExtension(
    /**
     * Controls `@Rpc` [annotation type-safety](https://github.com/Kotlin/kotlinx-rpc/pull/240) compile-time checkers.
     *
     * CAUTION: Disabling is considered unsafe.
     * This option is only needed to prevent cases where type-safety analysis fails and valid code can't be compiled.
     */
    val annotationTypeSafetyEnabled: Boolean? = null,
    /**
     * Strict mode settings.
     * Allows configuring the reporting state of deprecated features.
     */
    val strict: RpcStrictModeExtension? = null,
) {

    context(Project)
    @OptIn(RpcDangerousApi::class)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlinx.rpc.plugin") {
            project.rpc.annotationTypeSafetyEnabled tryAssign annotationTypeSafetyEnabled
            strict?.applyTo(project.rpc.strict)
        }
}
