package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.Named
import gradle.plugins.kotlin.KotlinCompilation
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = KotlinTargetSerializer::class)
internal interface KotlinTarget : Named {

    val targetName: String

    override val name: String
        get() = targetName

    /**
     * A container for [Kotlin compilations][KotlinCompilation] related to this target.
     *
     * Allows access to the default [main][KotlinCompilation.MAIN_COMPILATION_NAME] or [test][KotlinCompilation.TEST_COMPILATION_NAME]
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

private object KotlinTargetSerializer : JsonPolymorphicSerializer<KotlinTarget>(
    KotlinTarget::class,
)

internal object KotlinTargetTransformingSerializer : KeyTransformingSerializer<KotlinTarget>(
    KotlinTarget.serializer(),
    "type",
)

@Serializable
@SerialName("KotlinTarget")
internal data class KotlinTargetIml(
    override val compilations: List<KotlinCompilation>? = null,
) : KotlinTarget {

    override val targetName: String
        get() = ""
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> List<KotlinTarget>.instanceOf(): List<KotlinTarget> =
    filterIsInstance<T>() as List<KotlinTarget>
