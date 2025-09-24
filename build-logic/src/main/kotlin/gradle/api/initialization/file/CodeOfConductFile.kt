package gradle.api.initialization.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("CodeOfConductFile")
public data class CodeOfConductFile(
    val source: String = "files/CODE_OF_CONDUCT.md",
    override val into: String = "CODE_OF_CONDUCT.md",
    override val resolution: FileResolution = FileResolution.NEWER,
    val contactMethod: String? = null,
    val contactMethodPlaceholder: String = "[INSERT CONTACT METHOD]"
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val replace: Map<String, String> = listOfNotNull(
        contactMethod?.let { contactMethod -> contactMethodPlaceholder to contactMethod },
    ).toMap()
}
