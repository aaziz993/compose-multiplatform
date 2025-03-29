package gradle.plugins.kotlin.targets.nat

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = DefaultCInteropSettingsKeyTransformingSerializer::class)
internal data class DefaultCInteropSettings(
    override val name: String? = null,
    override val dependencyFiles: Set<String>? = null,
    override val setDependencyFiles: Set<String>? = null,
    override val defFile: String? = null,
    override val packageName: String? = null,
    override val headers: Set<String>? = null,
    override val includeDirs: List<String>? = null,
    override val compilerOpts: List<String>? = null,
    override val linkerOpts: List<String>? = null,
    override val extraOpts: List<String>? = null
) : CInteropSettings<org.jetbrains.kotlin.gradle.plugin.mpp.DefaultCInteropSettings>

private object DefaultCInteropSettingsKeyTransformingSerializer
    : CInteropSettingsKeyTransformingSerializer<DefaultCInteropSettings>(DefaultCInteropSettings.generatedSerializer())
