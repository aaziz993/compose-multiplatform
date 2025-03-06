package plugin.project.kotlin.kmp.model

import gradle.kotlin
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.kotlin.model.KotlinCompilation
import plugin.project.gradle.model.Named

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

    val needKMP: Boolean
        get() = true

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        named as org.jetbrains.kotlin.gradle.plugin.KotlinTarget

        this@KotlinTarget.compilations?.forEach { compilation ->
            named.compilations.named(compilation.name) {
                compilation.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() = applyTo(kotlin.targets)
}

private object KotlinTargetSerializer : JsonContentPolymorphicSerializer<KotlinTarget>(
    KotlinTarget::class,
)

internal object KotlinTargetTransformingSerializer : KeyTransformingSerializer<KotlinTarget>(
    KotlinTarget.serializer(),
    "type",
)

@Serializable
@SerialName("")
internal data class KotlinTargetIml(
    override val compilations: List<KotlinCompilation>? = null,
) : KotlinTarget {

    override val targetName: String
        get() = ""
}
