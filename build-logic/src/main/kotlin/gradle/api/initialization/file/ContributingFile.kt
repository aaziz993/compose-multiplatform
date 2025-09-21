package gradle.api.initialization.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public data class ContributingFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.NEWER,
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "CONTRIBUTING.md"
}
