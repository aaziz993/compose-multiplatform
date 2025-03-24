package gradle.plugins.kmp.web

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.PackageJson

// Gson set nulls reflectively no matter on default values and non-null types
@Serializable
internal data class PackageJson(
    val name: String? = null,
    val version: String? = null,
    val customFields: SerializableAnyMap? = null,
    val private: Boolean? = null,
    val main: String? = null,
    val workspaces: List<String>? = null,
    val overrides: Map<String, String>? = null,
    val types: String? = null,
    val saveTo: String? = null,
) {

    context(project: Project)
    fun applyTo(receiver: PackageJson) {
        json::name trySet name
        json::version trySet version
        customFields?.forEach(json::customField)
        json::private trySet private
        json::main trySet main
        json::workspaces trySet workspaces
        json::overrides trySet overrides
        json::types trySet types
        saveTo?.let(project::file)?.let(json::saveTo)
    }
}
