package gradle.accessors.catalog

import klib.data.type.addPrefix
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class Plugin(
    val id: String,
    override val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any? = null,
) : VersionCatalogDependency() {

    @Transient
    override lateinit var versionCatalog: VersionCatalog

    override val notation: String
        get() = "$id:$id.gradle.plugin${version?.addPrefix(":")}"
}
