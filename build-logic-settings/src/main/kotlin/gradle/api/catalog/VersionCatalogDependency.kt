package gradle.api.catalog

import kotlinx.serialization.Serializable
import kotlin.collections.get

internal abstract class VersionCatalogDependency {

    abstract var versionCatalog: VersionCatalog

    protected abstract val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any?

    val version
        get() = if (_version is Version) versionCatalog.versions[_version]
        else _version?.toString()
}
