package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationImpl
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = KotlinTargetSerializer::class)
internal interface KotlinTarget<T : org.jetbrains.kotlin.gradle.plugin.KotlinTarget> : ProjectNamed<T> {

    val targetName: String?
        get() = name

    /**
     * A container for [Kotlin compilations][KotlinCompilation] related to this target.
     *
     * Allows access to the default [main][KotlinCompilation.MAIN_COMPILATION_NAME] or [test][KotlinCompilation.TEST_COMPILATION_NAME]
     * compilations, or the creation of additional compilations.
     */
    val compilations: LinkedHashSet<out KotlinCompilation<out org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>>?

    val needKMP: Boolean
        get() = true

    context(Project)
    override fun applyTo(receiver: T) {
        compilations?.forEach { compilation ->
            (compilation as KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>)
                .applyTo(receiver.compilations)
        }
    }

    context(Project)
    fun applyTo()
}

private object KotlinTargetSerializer : JsonPolymorphicSerializer<KotlinTarget<*>>(
    KotlinTarget::class,
)

internal object KotlinTargetKeyTransformingSerializer : KeyTransformingSerializer<KotlinTarget<*>>(
    KotlinTargetSerializer,
    "type",
)

@Serializable
@SerialName("KotlinTarget")
internal data class KotlinTargetIml(
    override val name: String? = null,
    override val compilations: LinkedHashSet<KotlinCompilationImpl>? = null,
) : KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget> {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets) { _, _ -> }
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> Set<KotlinTarget<*>>.filterKotlinTargets(): List<KotlinTarget<*>> =
    filterIsInstance<T>() as List<KotlinTarget<*>>
