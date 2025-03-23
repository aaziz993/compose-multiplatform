package gradle.plugins.apple

import gradle.api.ProjectNamed
import gradle.api.file.SourceDirectorySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.gradle.apple.AppleSourceSet

@Serializable
internal data class AppleSourceSet(
    override val name: String? = null,
    val apple: SourceDirectorySet? = null,
) : ProjectNamed<AppleSourceSet> {

    context(Project)
    override fun applyTo(recipient: AppleSourceSet) {
        apple?.applyTo(recipient.apple)
    }
}
