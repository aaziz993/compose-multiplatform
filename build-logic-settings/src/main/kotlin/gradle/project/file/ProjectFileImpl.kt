package gradle.project.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class ProjectFileImpl(
    override val from: String,
    override val into: String,
    override val resolution: FileResolution = FileResolution.ABSENT,
    override val replace: Map<String, String> = emptyMap(),
    @Transient
    override val transform: ((String) -> String)? = null
) : ProjectFile
