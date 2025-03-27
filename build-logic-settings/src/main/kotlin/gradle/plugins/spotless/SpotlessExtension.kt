@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.ifTrue
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface SpotlessExtension {

    /** Line endings (if any). */
    val lineEndings: LineEnding?

    /** Sets the encoding to use. */
    val encoding: String?

    /**
     * Limits the target to only the files which have changed since the given git reference,
     * which is resolved according to <a href="https://javadoc.io/static/org.eclipse.jgit/org.eclipse.jgit/5.6.1.202002131546-r/org/eclipse/jgit/lib/Repository.html#resolve-java.lang.String-">this</a>
     */
    val ratchetFrom: String?

    /**
     * Configures Gradle's {@code check} task to run {@code spotlessCheck} if {@code true},
     * but to not do so if {@code false}.
     * <p>
     * {@code true} by default.
     */
    val enforceCheck: Boolean?
    val predeclareDepsFromBuildscript: Boolean?
    val predeclareDeps: Boolean?
    val formats: LinkedHashSet<@Serializable(with = FormatExtensionKeyTransformingSerializer::class) FormatExtension<out com.diffplug.gradle.spotless.FormatExtension>>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("spotless").id) {
            lineEndings?.let(project.spotless::setLineEndings)
            encoding?.let(project.spotless::setEncoding)
            ratchetFrom?.let(project.spotless::setRatchetFrom)
            enforceCheck?.let(project.spotless::setEnforceCheck)

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
