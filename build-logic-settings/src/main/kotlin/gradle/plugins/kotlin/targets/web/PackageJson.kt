package gradle.plugins.kotlin.targets.web

import klib.data.type.collection.SerializableAnyMap
import klib.data.type.reflection.trySet
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

    context(Project)
    fun applyTo(receiver: PackageJson) {
        receiver::name trySet name
        receiver::version trySet version
        customFields?.forEach(receiver::customField)
        receiver::private trySet private
        receiver::main trySet main
        receiver::workspaces trySet workspaces
        receiver::overrides trySet overrides
        receiver::types trySet types
        receiver::saveTo trySet saveTo?.let(project::file)
    }
}
