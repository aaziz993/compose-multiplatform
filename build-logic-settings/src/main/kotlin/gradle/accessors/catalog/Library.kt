package gradle.accessors.catalog

import gradle.addPrefix
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class Library(
    @SerialName("group")
    private val _group: String? = null,
    @SerialName("name")
    private val _name: String? = null,
    @SerialName("module")
    private val _module: String? = null,
    override val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any? = null,
) : VersionCatalogMember() {

    @Transient
    override lateinit var versionCatalog: VersionCatalog

    override val notation: String
        get() = "$module${version?.addPrefix(":")}"

    val group by lazy {
        _group ?: _module!!.substringBefore(":")
    }

    val name by lazy {
        _name ?: _module!!.substringAfter(":")
    }

    val module by lazy {
        _module ?: "$_group:$_name"
    }
}
