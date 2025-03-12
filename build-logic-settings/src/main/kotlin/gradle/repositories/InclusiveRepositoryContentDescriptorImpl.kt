package gradle.repositories

import kotlinx.serialization.Serializable

@Serializable
internal data class InclusiveRepositoryContentDescriptorImpl(
    override val includeGroups: List<String>? = null,
    override val includeGroupsAndSubgroups: List<String>? = null,
    override val includeGroupsByRegexes: List<String>? = null,
    override val includeModules: List<Module>? = null,
    override val includeModulesByRegexes: List<Module>? = null,
    override val includeVersions: List<Version>? = null,
    override val includeVersionsByRegexes: List<Version>? = null,
) : InclusiveRepositoryContentDescriptor
