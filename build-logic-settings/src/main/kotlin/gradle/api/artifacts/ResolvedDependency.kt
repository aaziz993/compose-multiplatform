package gradle.api.artifacts

import kotlinx.serialization.Serializable

@Serializable
internal data class ResolvedDependency(
    val name: String? = null,
    val moduleGroup: String? = null,
    val moduleName: String? = null,
    val moduleVersion: String? = null,
    val configuration: String? = null,
    val module: ResolvedModuleVersion? = null,
    val children: Set<ResolvedDependency>? = null,
    val parents: Set<ResolvedDependency>? = null,
    val moduleArtifacts: Set<ResolvedArtifact>? = null,
    val allModuleArtifacts: Set<ResolvedArtifact>? = null,
) {

    fun equals(other: org.gradle.api.artifacts.ResolvedDependency): Boolean =
        (name ?: other.name) == other.name
            && (moduleGroup ?: other.moduleGroup) == other.moduleGroup
            && (moduleName ?: other.moduleName) == other.moduleName
            && (moduleVersion ?: other.moduleVersion) == other.moduleVersion
            && (configuration ?: other.configuration) == other.configuration
            && module?.equals(other.module) != false
            && children?.all { resolveDependency -> other.children.any(resolveDependency::equals) } != false
            && parents?.all { resolveDependency -> other.parents.any(resolveDependency::equals) } != false
            && moduleArtifacts?.all { resolveDependency -> other.moduleArtifacts.any(resolveDependency::equals) } != false
            && allModuleArtifacts?.all { resolveDependency -> other.allModuleArtifacts.any(resolveDependency::equals) } != false
}
