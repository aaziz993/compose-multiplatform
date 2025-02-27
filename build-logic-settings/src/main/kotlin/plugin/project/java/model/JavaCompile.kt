package plugin.project.java.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import plugin.project.gradle.model.AbstractCompile
import plugin.project.gradle.model.CompileOptions
import plugin.project.gradle.model.HasCompileOptions

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
    fun applyTo(compile: JavaCompile) {
        super<AbstractCompile>.applyTo(compile)
        super<HasCompileOptions>.applyTo(compile)

        modularity?.applyTo(compile.modularity)
    }
}
