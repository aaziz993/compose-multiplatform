package gradle.plugins.kotlin.tasks

import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents a Kotlin task using the [Gradle toolchains for JVM projects](https://docs.gradle.org/current/userguide/toolchains.html)
 * feature inside its [TaskAction](https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/TaskAction.html).
 *
 * The Gradle toolchains for JVM projects feature is used by our plugin to compile Kotlin/JVM.
 *
 * Use this interface to configure different tasks to use different JDK versions via
 * [Gradle's tasks API](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:configuring_tasks).
 */
internal interface UsesKotlinJavaToolchain<T : org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain> : Task<T> {

    /**
     * A helper shortcut to get [KotlinJavaToolchain] from [kotlinJavaToolchainProvider] without calling the `.get()` method.
     */

    val kotlinJavaToolchain: KotlinJavaToolchain?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        kotlinJavaToolchain?.applyTo(receiver.kotlinJavaToolchain)
    }
}

@Serializable
@SerialName("UsesKotlinJavaToolchain")
internal data class UsesKotlinJavaToolchainImpl(
    override val kotlinJavaToolchain: KotlinJavaToolchain? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
) : UsesKotlinJavaToolchain<org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain> {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain>())
}
