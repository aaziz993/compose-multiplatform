package gradle.plugins.apple

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.file.SourceDirectorySet
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = AppleSourceSetObjectTransformingSerializer::class)
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

private object AppleSourceSetObjectTransformingSerializer : NamedObjectTransformingSerializer<AppleSourceSet>(
    AppleSourceSet.generatedSerializer(),
)
