package gradle.api.initialization.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("LicenseFile")
public data class LicenseFile(
    val source: String,
    override val into: String = "LICENSE",
    override val resolution: FileResolution = FileResolution.NEWER,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]"
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val replace: Map<String, String> = listOfNotNull(
        year?.let { year -> yearPlaceholder to year },
        owner?.let { owner -> ownerPlaceholder to owner },
    ).toMap()
}
