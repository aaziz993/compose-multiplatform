package gradle.plugins.kotlin

import gradle.accessors.kotlin
import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import gradle.api.trySet
import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = KotlinTargetObjectTransformingContentPolymorphicSerializer::class)
internal interface KotlinTarget<T : org.jetbrains.kotlin.gradle.plugin.KotlinTarget> : ProjectNamed<T> {

    val targetName: String?
        get() = name

    /**
     * Configures the publication of sources.
     *
     * @param publish Indicates whether the sources JAR is to be published. Defaults to `true`.
     */
    val withSourcesJar: Boolean?

    val mavenPublication: MavenPublication?

    /**
     * A container for [Kotlin compilations][KotlinCompilation] related to this target.
     *
     * Allows access to the default [main][KotlinCompilation.MAIN_COMPILATION_NAME] or [test][KotlinCompilation.TEST_COMPILATION_NAME]
     * compilations, or the creation of additional compilations.
     */
    val compilations: LinkedHashSet<out KotlinCompilation<out org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>>?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::withSourcesJar trySet withSourcesJar

        mavenPublication?.let { mavenPublication ->
            receiver.mavenPublication {
                mavenPublication.applyTo(this)
            }
        }

        compilations?.forEach { compilation ->
            (compilation as KotlinCompilation<org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>)
                .applyTo(receiver.compilations)
        }
    }

    context(Project)
    fun applyTo()
}

private class KotlinTargetObjectTransformingContentPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : JsonObjectTransformingContentPolymorphicSerializer<KotlinTarget<*>>(KotlinTarget::class)

@Serializable
@SerialName("KotlinTarget")
internal data class KotlinTargetIml(
    override val name: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<KotlinCompilationImpl>? = null,
) : KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget> {

    context(Project)
    override fun applyTo() =
        applyTo(project.kotlin.targets) { _, _ -> }
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> Set<KotlinTarget<*>>.filterKotlinTargets(): List<KotlinTarget<*>> =
    filterIsInstance<T>() as List<KotlinTarget<*>>
