package plugin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class Dependency(
    val notation: String
) {

    @SerialName("file")
    internal class File(notation: String) : Dependency(notation)

    @SerialName("maven")
    internal class Maven(notation: String) : Dependency(notation)
}
