package gradle.plugins.kotlin

import gradle.api.applyTo
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.KotlinTargetKeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetsContainer

internal interface KotlinTargetsContainer<T : KotlinTargetsContainer> {

    /**
     * A [NamedDomainObjectContainer] containing all registered [Kotlin targets][KotlinTarget] in this project.
     */
    val targets: LinkedHashSet<@Serializable(with = KotlinTargetKeyTransformingSerializer::class) KotlinTarget<out org.jetbrains.kotlin.gradle.plugin.KotlinTarget>>?

    context(Project)
    fun applyTo(receiver: T) {
        targets?.forEach { target ->
            (target as KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget>).applyTo(receiver.targets) { _, _ -> }
        }
    }
}
