@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
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
    val formats: LinkedHashSet<@Serializable(with = FormatExtensionTransformingSerializer::class) FormatExtension>?

    context(Project)
    fun applyTo() {
        lineEndings?.let(spotless::setLineEndings)
        encoding?.let(spotless::setEncoding)
        ratchetFrom?.let(spotless::setRatchetFrom)
        enforceCheck?.let(spotless::setEnforceCheck)

        // Applicable only in root project.
        if (project == rootProject) {
            predeclareDeps?.takeIf { it }?.run { spotless.predeclareDeps() }
            predeclareDepsFromBuildscript?.takeIf { it }?.run { spotless.predeclareDepsFromBuildscript() }
        }

        // Format files
        formats?.forEach { format ->
            format.applyTo()
        }
    }
}
