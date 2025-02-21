package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.JavaExtension
import kotlinx.serialization.Serializable

@Serializable
internal class CleanthatJavaConfig(
    val groupArtifact: String? = null,
    val version: String? = null,
    val sourceJdk: String? = null,
    val mutators: List<String?>? = null,
    val excludedMutators: List<String?>? = null,
    val includeDraft: Boolean? = null
){
    fun applyTo(format: JavaExtension.CleanthatJavaConfig){
        groupArtifact?.let(format::groupArtifact)
        version?.let(format::version)
        sourceJdk?.let(format::sourceCompatibility)
        mutators?.let(format::addMutators)
        excludedMutators?.forEach(format::excludeMutator)
        includeDraft?.let(format::includeDraft)
    }
}
