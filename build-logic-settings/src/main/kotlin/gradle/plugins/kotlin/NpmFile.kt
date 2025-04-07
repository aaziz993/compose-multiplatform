package gradle.plugins.kotlin

import gradle.api.artifacts.Dependency
import kotlinx.serialization.Serializable

@Serializable
internal data class NpmFile(
    override val because: String? = null,
    val name: String? = null,
    val directory: String,
) : Dependency<org.gradle.api.artifacts.Dependency>
