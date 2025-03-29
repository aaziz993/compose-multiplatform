package gradle.plugins.kotlin.targets.nat

import gradle.api.NamedObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = DefaultCInteropSettingsObjectTransformingSerializer::class)
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

private object DefaultCInteropSettingsObjectTransformingSerializer
    : NamedObjectTransformingSerializer<DefaultCInteropSettings>(DefaultCInteropSettings.generatedSerializer())
