package gradle.api.ci

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.Project

@Serializable
internal data class Versioning(
    val branch: Boolean = true,
    val run: Boolean = true,
) {

    @Transient
    lateinit var ci: CI

    context(Project)
    val buildMetadata: String
        get() = "${ci.branch?.takeIf { branch }.orEmpty()}${ci.run?.takeIf { run }.orEmpty()}"
}
