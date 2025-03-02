package plugin.project.kotlin.kmp.model

import gradle.kotlin
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.kotlin.model.Named
import plugin.project.kotlin.model.configure

@Serializable(with = KotlinTargetSerializer::class)
internal interface KotlinTarget : Named {

    val targetName: String

    override val name: String
        get() = targetName

    /**
     * A container for [Kotlin compilations][plugin.project.kotlin.model.KotlinCompilation] related to this target.
     *
     * Allows access to the default [main][plugin.project.kotlin.model.KotlinCompilation.MAIN_COMPILATION_NAME] or [test][plugin.project.kotlin.model.KotlinCompilation.TEST_COMPILATION_NAME]
     * compilations, or the creation of additional compilations.
     */
    val compilations: List<KotlinCompilation>?

    context(Project)
    fun applyTo(targets: NamedDomainObjectCollection<out org.jetbrains.kotlin.gradle.plugin.KotlinTarget>) =
        targets.configure {
            this@KotlinTarget.compilations?.forEach { compilation ->
                compilation.applyTo(compilations)
            }
        }

    context(Project)
    fun applyTo()
}

private object KotlinTargetSerializer : JsonContentPolymorphicSerializer<KotlinTarget>(
    KotlinTarget::class,
)

internal object KotlinTargetTransformingSerializer : KeyTransformingSerializer<KotlinTarget>(
    KotlinTarget.serializer(),
    "type",
)
