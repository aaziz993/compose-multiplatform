package gradle.plugins.web.js

import gradle.plugins.kotlin.KotlinCompilationTask
import gradle.plugins.kmp.web.KotlinJsCompilerOptions
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface KotlinJsCompile : KotlinCompilationTask<KotlinJsCompilerOptions>

@Serializable
@SerialName("KotlinJsCompile")
internal data class KotlinJsCompileImpl(
    override val compilerOptions: KotlinJsCompilerOptions? = null,
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
    override val name: String = ""
) : KotlinJsCompile
