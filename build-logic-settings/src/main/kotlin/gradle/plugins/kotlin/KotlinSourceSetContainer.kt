package gradle.plugins.kotlin

import gradle.api.applyTo
import gradle.plugins.kmp.KotlinSourceSet
import gradle.plugins.kmp.KotlinSourceSetKeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer


/**
 * Represents a Kotlin DSL entity containing a collection of multiple [KotlinSourceSets][KotlinSourceSet].
 */
internal interface KotlinSourceSetContainer<T : KotlinSourceSetContainer> {

    /**
     * A [NamedDomainObjectContainer] containing all registered [KotlinSourceSets][KotlinSourceSet] in this project.
     */
    val sourceSets: LinkedHashSet<@Serializable(with = KotlinSourceSetKeyTransformingSerializer::class) KotlinSourceSet>?

    context(Project)
    fun applyTo(receiver: KotlinSourceSetContainer) {
        sourceSets?.forEach { sourceSet ->
            sourceSet.applyTo(receiver.sourceSets)
        }
    }
}
