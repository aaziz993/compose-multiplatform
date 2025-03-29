package gradle.plugins.kotlin.mpp

import gradle.api.publish.maven.MavenPublication
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class Metadata(
    override val onPublicationCreated: String? = null,
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<KotlinMetadataCompilation>? = null,
    override val name: String? = null,
) : KotlinOnlyTarget<
    org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataCompilation<*>>,
    org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataCompilation<*>,
    >() {

    context(Project)
    override fun applyTo() =
        throw UnsupportedOperationException("Can't determine metadata targets")
}
