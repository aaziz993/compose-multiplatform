package gradle.api.catalog

import kotlinx.serialization.Serializable
import kotlin.collections.get
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

internal abstract class VersionCatalogNotation {

    abstract var versionCatalog: VersionCatalog

    protected abstract val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any?

    val version
        get() = if (_version is Version) versionCatalog.versions[_version]
        else _version?.toString()



    context(Settings)
    abstract val notation: Any

    context(Project)
    abstract val notation: Any
}
