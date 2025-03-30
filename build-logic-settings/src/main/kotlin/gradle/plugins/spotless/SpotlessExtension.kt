@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
import gradle.api.trySet
import gradle.ifTrue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SpotlessExtension(
    /** Line endings (if any). */
    val lineEndings: LineEnding? = null,
    /** Sets the encoding to use. */
    val encoding: String? = null,
    /**
     * Limits the target to only the files which have changed since the given git reference,
     * which is resolved according to <a href="https://javadoc.io/static/org.eclipse.jgit/org.eclipse.jgit/5.6.1.202002131546-r/org/eclipse/jgit/lib/Repository.html#resolve-java.lang.String-">this</a>
     */
    val ratchetFrom: String? = null,
    /**
     * Configures Gradle's {@code check} task to run {@code spotlessCheck} if {@code true},
     * but to not do so if {@code false}.
     * <p>
     * {@code true} by default.
     */
    val enforceCheck: Boolean? = null,
    val predeclareDepsFromBuildscript: Boolean? = null,
    val predeclareDeps: Boolean? = null,
    val formats: LinkedHashSet<FormatExtension<out @Contextual com.diffplug.gradle.spotless.FormatExtension>>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("com.diffplug.spotless") {
            project.spotless::setLineEndings trySet lineEndings
            encoding?.let(project.spotless::setEncoding)
            project.spotless::setRatchetFrom trySet ratchetFrom
            project.spotless::setEnforceCheck trySet enforceCheck

            // Applicable only in root project.
            predeclareDeps?.ifTrue(project.spotless::predeclareDeps)
            // Applicable only in root project.
            predeclareDepsFromBuildscript?.ifTrue(project.spotless::predeclareDepsFromBuildscript)

            // Format files
            formats?.forEach { format ->
                format.applyTo()
            }
        }
}
