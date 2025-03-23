package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.plugins.kotlin.KotlinCompilation
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = KotlinTargetSerializer::class)
internal interface KotlinTarget<
    T : org.jetbrains.kotlin.gradle.plugin.KotlinTarget,
    C : org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>
    > : ProjectNamed<T> {

    val targetName: String

    override val name: String
        get() = targetName

    /**
     * A container for [Kotlin compilations][KotlinCompilation] related to this target.
     *
     * Allows access to the default [main][KotlinCompilation.MAIN_COMPILATION_NAME] or [test][KotlinCompilation.TEST_COMPILATION_NAME]
     * compilations, or the creation of additional compilations.
     */
    val compilations: List<KotlinCompilation<C>>?

    val needKMP: Boolean
        get() = true

    context(Project)
    override fun applyTo(receiver: T) {
        this@KotlinTarget.compilations?.forEach { compilation ->
            receiver.compilations.named(compilation.name) {
                compilation.applyTo(this)
            }
        }
    }

    context(Project)
    fun applyTo()
}

private object KotlinTargetSerializer : JsonPolymorphicSerializer<KotlinTarget<*>>(
    KotlinTarget::class,
)

internal object KotlinTargetTransformingSerializer : KeyTransformingSerializer<KotlinTarget<*>>(
    KotlinTargetSerializer,
    "type",
)

@Serializable
@SerialName("KotlinTarget")
internal data class KotlinTargetIml(
    override val compilations: List<KotlinCompilation>? = null,
) : KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget> {

    override val targetName: String
        get() = ""

    context(Project)
    override fun applyTo() {
        applyTo(kotlin.targets) { _, _ -> }
    }
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> List<KotlinTarget<*>>.instanceOf(): List<KotlinTarget<*>> =
    filterIsInstance<T>() as List<KotlinTarget<*>>
