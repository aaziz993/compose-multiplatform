package gradle.plugins.apple

import gradle.api.ProjectNamed
import gradle.api.file.SourceDirectorySet
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSourceSet(
    val apple: SourceDirectorySet? = null,
    val sourceSetName: String? = null,
) : ProjectNamed<org.jetbrains.gradle.apple.AppleSourceSet> {

    override val name: String?
        get() = sourceSetName

    context(Project)
    override fun applyTo(recipient: org.jetbrains.gradle.apple.AppleSourceSet) {
        apple?.applyTo(recipient.apple)
    }
}

internal object AppleSourceSetTransformingSerializer : KeyTransformingSerializer<AppleSourceSet>(
    AppleSourceSet.serializer(),
    "sourceSetName",
)
