package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinJsBinaryContainer(
    val executable: Executable = Executable(),
    val library: Library = Library(),
)
