package gradle.plugins.apple

import gradle.api.ProjectNamed
import gradle.api.file.SourceDirectorySet
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSourceSet(
    override val name: String? = null,
    val apple: SourceDirectorySet? = null,
) : ProjectNamed<org.jetbrains.gradle.apple.AppleSourceSet> {

    val sourceSetName: String?
        get() = name

    context(Project)
    override fun applyTo(receiver: org.jetbrains.gradle.apple.AppleSourceSet) {
        apple?.applyTo(receiver.apple)
    }
}

internal object AppleSourceSetTransformingSerializer : KeyTransformingSerializer<AppleSourceSet>(
    AppleSourceSet.serializer(),
    "sourceSetName",
)
