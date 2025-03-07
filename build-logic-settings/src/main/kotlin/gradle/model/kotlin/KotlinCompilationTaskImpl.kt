package gradle.model.kotlin

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

/**
 * Represents a Kotlin task compiling using configurable [compilerOptions].
 *
 * See [KotlinCommonCompilerOptions] and its inheritors for possible compiler options.
 *
 * @see [KotlinCommonCompilerOptions]
 */
@Serializable
internal data class KotlinCompilationTaskImpl<out CO : KotlinCommonCompilerOptions>(
    override val name: String = "",
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    /**
     * Represents the compiler options used by a Kotlin compilation process.
     *
     * This can be used to get the values of currently configured options or modify them.
     */
    override val compilerOptions: CO?
) : KotlinCompilationTask<CO>
