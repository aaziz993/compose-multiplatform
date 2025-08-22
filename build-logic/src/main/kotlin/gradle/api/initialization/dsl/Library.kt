package gradle.api.initialization.dsl

import gradle.api.artifacts.VersionConstraint
import klib.data.type.primitives.string.addPrefix
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import kotlinx.serialization.Serializable

@Serializable
public data class Library(
    val group: String,
    val name: String,
    val version: VersionConstraint? = null,
) {

    override fun toString(): String = "$group:$name${version.toString().addPrefixIfNotEmpty(":")}"
}
