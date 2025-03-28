package gradle.accessors.catalog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal abstract class VersionCatalogDependency {

    abstract var versionCatalog: VersionCatalog

    @SerialName("version")
    protected abstract val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any?

    val version
        get() = if (_version is Version) versionCatalog.versions[_version]
        else _version?.toString()

    abstract val notation: String
}
