package plugin.project.kotlin.rpc.model

internal interface RpcExtension {

    /**
     * Controls `@Rpc` [annotation type-safety](https://github.com/Kotlin/kotlinx-rpc/pull/240) compile-time checkers.
     *
     * CAUTION: Disabling is considered unsafe.
     * This option is only needed to prevent cases where type-safety analysis fails and valid code can't be compiled.
     */
    val annotationTypeSafetyEnabled: Boolean ?

    /**
     * Strict mode settings.
     * Allows configuring the reporting state of deprecated features.
     */
    val strict: RpcStrictModeExtension?
}
