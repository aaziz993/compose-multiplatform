package gradle.plugins.kotlin.mpp

import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.KotlinCompilationOutput
import gradle.plugins.kotlin.KotlinSourceSet
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import gradle.plugins.project.Dependency
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataCompilation

@Serializable
internal data class KotlinMetadataCompilation(
    override val defaultSourceSet: KotlinSourceSet? = null,
    override val compileDependencyFiles: Set<String>? = null,
    override val setCompileDependencyFiles: Set<String>? = null,
    override val output: KotlinCompilationOutput? = null,
    override val compileTaskProvider: KotlinCompilationTask<out org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>, *>? = null,
    override val associatedCompilations: Set<String>? = null,
    override val dependencies: Set<Dependency>? = null,
    override val name: String? = null,
) : KotlinCompilation<KotlinMetadataCompilation<*>>
