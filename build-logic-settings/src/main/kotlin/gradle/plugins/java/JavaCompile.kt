package gradle.plugins.java

import gradle.collection.SerializableAnyMap
import gradle.api.tasks.AbstractCompile
import gradle.api.tasks.compile.CompileOptions
import gradle.api.tasks.compile.HasCompileOptions
import kotlinx.serialization.Serializable
import org.gradle.api.Named
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
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val destinationDirectory: String? = null,
    override val classpath: List<String>? = null,
    override val sourceCompatibility: String? = null,
    override val targetCompatibility: String? = null,
    override val options: CompileOptions? = null,
    /**
     * Sets the module path handling of this compile task.
     *
     * @since 6.4
     */
    val modularity: ModularitySpec? = null,
) : AbstractCompile(), HasCompileOptions {

    context(Project)
    override fun applyTo(named: Named) {
        super<AbstractCompile>.applyTo(named)

        named as JavaCompile

        super<HasCompileOptions>.applyTo(named)

        modularity?.applyTo(named.modularity)
    }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<JavaCompile>())
}
