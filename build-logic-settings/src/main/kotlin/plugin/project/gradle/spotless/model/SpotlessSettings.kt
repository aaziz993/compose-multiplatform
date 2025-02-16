package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding
import kotlinx.serialization.Serializable

@Serializable
internal data class SpotlessSettings(
    val enabled: Boolean = true,
    /** Line endings (if any). */
     val lineEndings: LineEnding? = null,
    /** Returns the encoding to use. */
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
     val formats: Map<String, FormatSettings>? = null,
     val kotlinGradle: KotlinGradleExtension? = null
)
