package gradle.accessors.catalog

import gradle.accessors.settings
import gradle.isPath
import gradle.isValidUrl
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.jetbrains.amper.gradle.getBindingMap
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Serializable
internal data class VersionCatalog(
    val name: String,
    val versions: Map<String, String> = emptyMap(),
    val libraries: Map<String, Library> = emptyMap(),
    val plugins: Map<String, Plugin> = emptyMap()
) {

    init {
        libraries.values.forEach { library ->
            library.versionCatalog = this
        }

        plugins.values.forEach { plugin ->
            plugin.versionCatalog = this
        }
    }

    fun version(alias: String) = alias.asVersionCatalogAlias.let { alias ->
        versions[alias] ?: error("Version '$alias' not found in version catalog: $name")
    }

    fun library(alias: String): Library = alias.asVersionCatalogAlias.let { alias ->
        libraries[alias] ?: error("Library  '$alias'  not found in version catalog: $name")
    }

    fun plugin(alias: String): Plugin = alias.asVersionCatalogAlias.let { alias ->
        plugins[alias] ?: error("Plugin '$alias' not found in version catalog: $name")
    }
}

private const val VERSION_CATALOG = "version.catalog"

@Suppress("UNCHECKED_CAST")
internal val Settings.allLibs: MutableSet<VersionCatalog>
    get() = extraProperties.getBindingMap(VERSION_CATALOG)

internal val Settings.libs: VersionCatalog
    get() = allLibs.versionCatalog("libs")!!

internal fun Set<VersionCatalog>.versionCatalog(name: String) =
    find { versionCatalog -> versionCatalog.name == name }

internal fun Set<VersionCatalog>.resolveDependency(
    notation: String,
    directory: Directory,
): Any = when {
    notation.startsWith("$") -> resolveRef(notation, VersionCatalog::library).notation
    notation.isPath -> directory.file(notation)
    notation.isValidUrl -> notation.asVersionCatalogUrl
    else -> notation
}

private val String.asVersionCatalogUrl: String
    get() {
        val fileNamePart = substringAfter(":", "").replace(":", "-")

        return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
            substringAfterLast(":", "")
        }/$fileNamePart.toml"
    }

context(Project)
internal fun String.resolvePlugin() =
    if (startsWith("$")) project.settings.allLibs.resolveRef(removePrefix("$"), VersionCatalog::plugin).id
    else this

context(Project)
internal fun String.resolveVersion() =
    if (startsWith("$")) project.settings.allLibs.resolveRef(removePrefix("$"), VersionCatalog::version)
    else this

private fun <T> Set<VersionCatalog>.resolveRef(
    ref: String,
    resolver: VersionCatalog.(name: String) -> T
): T {
    val versionCatalogName = ref
        .removePrefix("$")
        .substringBefore(".")
    val name = ref.substringAfter(".", "")

    return resolver(
            (versionCatalog(versionCatalogName)
                    ?: error("Not found version catalog: $versionCatalogName")),
            name,
    )
}

private
val String.asVersionCatalogAlias
    get() = replace(".", "-")

