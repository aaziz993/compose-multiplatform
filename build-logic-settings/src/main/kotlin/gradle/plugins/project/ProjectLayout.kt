package gradle.plugins.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class ProjectLayout {

    @Serializable
    @SerialName("default")
    internal object Default : ProjectLayout()

    @Serializable
    @SerialName("flat")
    internal data class Flat(
        val targetDelimiter: String = "@",
        val androidVariantDelimiter: String = "+"
    ) : ProjectLayout()
}
