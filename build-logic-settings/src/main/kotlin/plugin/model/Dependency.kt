package plugin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class Dependency{
    abstract val notation: String

    @Serializable
    @SerialName("file")
    internal data class File(override val notation: String) : Dependency()

    @Serializable
    @SerialName("maven")
    internal class Maven(override val notation: String) : Dependency()
}
