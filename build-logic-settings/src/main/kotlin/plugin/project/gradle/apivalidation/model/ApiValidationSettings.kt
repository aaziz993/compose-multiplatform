package plugin.project.gradle.apivalidation.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiValidationSettings(
    override val validationDisabled: Boolean? = null,
    override val ignoredPackages: Set<String>? = null,
    override val ignoredProjects: Set<String>? = null,
    override val nonPublicMarkers: Set<String>? = null,
    override val ignoredClasses: Set<String>? = null,
    override val publicMarkers: Set<String>? = null,
    override val publicPackages: Set<String>? = null,
    override val publicClasses: Set<String>? = null,
    override val additionalSourceSets: Set<String>? = null,
    override val apiDumpDirectory: String? = null,
    override val klib: KlibValidationSettings? = null,
    val enabled: Boolean = true
) : ApiValidationExtension
