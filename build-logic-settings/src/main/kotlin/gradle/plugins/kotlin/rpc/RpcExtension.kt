package gradle.plugins.kotlin.rpc

import gradle.accessors.rpc
import gradle.api.tryAssign
import kotlinx.rpc.RpcDangerousApi
import org.gradle.api.Project

internal interface RpcExtension {

    /**
     * Controls `@Rpc` [annotation type-safety](https://github.com/Kotlin/kotlinx-rpc/pull/240) compile-time checkers.
     *
     * CAUTION: Disabling is considered unsafe.
     * This option is only needed to prevent cases where type-safety analysis fails and valid code can't be compiled.
     */
    val annotationTypeSafetyEnabled: Boolean?

    /**
     * Strict mode settings.
     * Allows configuring the reporting state of deprecated features.
     */
    val strict: RpcStrictModeExtension?

    context(Project)
    @OptIn(RpcDangerousApi::class)
    fun applyTo() {
        rpc.annotationTypeSafetyEnabled tryAssign annotationTypeSafetyEnabled

        strict?.let { strict ->
            rpc.strict(strict::applyTo)
        }
    }
}
