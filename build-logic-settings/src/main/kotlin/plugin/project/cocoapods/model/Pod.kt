package plugin.project.cocoapods.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Pod(
    val name: String,
    val version: String? = null,
    val path: String? = null,
    val moduleName: String = name.asModuleName(),
    val headers: String? = null,
    val linkOnly: Boolean = false,
) {

    companion object {

        private fun String.asModuleName() = this
            .split("/")[0]     // Pick the module name from a subspec name.
            .replace('-', '_') // Support pods with dashes in names (see https://github.com/JetBrains/kotlin-native/issues/2884).
    }
}
