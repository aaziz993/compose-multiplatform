package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable

@Serializable
internal data class DefaultCInteropSettings(
    override val name: String = "",
    override val dependencyFiles: List<String>? = null,
    override val defFile: String? = null,
    override val packageName: String? = null,
    override val headers: List<String>? = null,
    override val includeDirs: List<String>? = null,
    override val includeDirectories: CInteropSettings.IncludeDirectories? = null,
    override val compilerOpts: List<String>? = null,
    override val linkerOpts: List<String>? = null,
    override val extraOpts: List<String>? = null
) : CInteropSettings
