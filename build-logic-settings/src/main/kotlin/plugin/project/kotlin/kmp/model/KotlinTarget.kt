package plugin.project.kotlin.kmp.model

import gradle.namedOrAll
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.model.Named

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
    abstract val compilations: List<plugin.project.kotlin.model.KotlinCompilation>?

    context(Project)
    fun applyTo(target: org.jetbrains.kotlin.gradle.plugin.KotlinTarget) {
        compilations?.forEach { compilation ->
            target.compilations.namedOrAll(compilation.name) {
                compilation.applyTo(this)
            }
        }
    }

    context(Project)
    abstract fun applyTo()
}

private object KotlinTargetSerializer : JsonContentPolymorphicSerializer<KotlinTarget>(
    KotlinTarget::class,
)

internal object KotlinTargetTransformingSerializer : KeyTransformingSerializer<KotlinTarget>(
    KotlinTarget.serializer(),
    "type",
)
