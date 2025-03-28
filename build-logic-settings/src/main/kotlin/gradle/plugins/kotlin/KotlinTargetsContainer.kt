package gradle.plugins.kotlin

import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface KotlinTargetsContainer {

    /**
     * A [NamedDomainObjectContainer] containing all registered [Kotlin targets][KotlinTarget] in this project.
     */
    val targets: LinkedHashSet<@Serializable(with = KotlinTargetKeyTransformingSerializer::class) KotlinTarget<out org.jetbrains.kotlin.gradle.plugin.KotlinTarget>>?

    context(Project)
    fun applyTo() {
        targets?.forEach { target ->
            (target as KotlinTarget<org.jetbrains.kotlin.gradle.plugin.KotlinTarget>).applyTo()
        }
    }
}
