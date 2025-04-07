package gradle.plugins.kotlin

import gradle.api.artifacts.Dependency
import kotlinx.serialization.Serializable

@Serializable
internal data class Npm(
    override val because: String? = null,
    val name: String,
    val version: String,
) : Dependency<org.gradle.api.artifacts.Dependency>
