package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding

internal interface SpotlessExtension {
    /** Line endings (if any). */
    val lineEndings: LineEnding?

    /** Returns the encoding to use. */
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
    val formats: Map<String, FormatSettings>?
    val kotlinGradle: KotlinGradleExtension?
}
