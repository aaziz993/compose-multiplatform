package gradle.project

internal sealed class ProjectLayout {

    internal object Default : ProjectLayout()

    internal data class Flat(
        val targetDelimiter: String = "@",
        val androidVariantDelimiter: String = "+"
    ) : ProjectLayout()
}
