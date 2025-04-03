package gradle.plugins.java

import gradle.api.tasks.applyTo
import gradle.api.tasks.compile.AbstractCompile
import gradle.api.tasks.compile.CompileOptions
import gradle.api.tasks.compile.HasCompileOptions
import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

/**
 * Compiles Java source files.
 *
 * <pre class='autoTested'>
 * plugins {
 * id 'java'
 * }
 *
 * tasks.withType(JavaCompile).configureEach {
 * //enable compilation in a separate daemon process
 * options.fork = true
 * }
</pre> *
 */
@Serializable
internal data class JavaCompile(
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
    override val destinationDirectory: String? = null,
    override val classpath: Set<String>? = null,
    override val sourceCompatibility: String? = null,
    override val targetCompatibility: String? = null,
    override val options: CompileOptions? = null,
    override val sourceFiles: Set<String>? = null,
    override val setSourceFiles: Set<String>? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    /**
     * Sets the module path handling of this compile task.
     *
     * @since 6.4
     */
    val modularity: ModularitySpec? = null,
) : AbstractCompile<JavaCompile>(), HasCompileOptions<JavaCompile> {

    context(Project)
    override fun applyTo(receiver: JavaCompile) {
        super<AbstractCompile>.applyTo(receiver)
        super<HasCompileOptions>.applyTo(receiver)

        modularity?.applyTo(receiver.modularity)
    }

    context(Project)
    override fun applyTo() = applyTo(project.tasks.withType<JavaCompile>())
}
