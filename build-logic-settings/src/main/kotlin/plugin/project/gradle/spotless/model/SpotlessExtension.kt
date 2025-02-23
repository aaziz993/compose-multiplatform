@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.gradle.spotless.JavascriptExtension
import com.diffplug.gradle.spotless.JsonExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.TypescriptExtension
import com.diffplug.gradle.spotless.YamlExtension
import com.diffplug.spotless.LineEnding
import com.diffplug.spotless.generic.LicenseHeaderStep
import com.diffplug.spotless.kotlin.KotlinConstants
import gradle.libs
import org.gradle.api.Project
import plugin.project.gradle.spotless.LICENSE_HEADER_DIR

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

    context(Project)
    fun applyTo(extension: SpotlessExtension) {
        val formats = mapOf(
            JavaExtension.NAME to FormatSettings(
                target = listOf("**/*.java"),
                trimTrailingWhitespace = true,
                endWithNewline = true,
                licenseHeader = LicenseHeaderConfig(
                    headerFile = LICENSE_HEADER_DIR,
                    delimiter = LicenseHeaderStep.DEFAULT_JAVA_HEADER_DELIMITER,
                ),
                importOrder = ImportOrderConfig(),
                removeIfUnusedImports = true,
                googleJavaFormat = GoogleJavaFormatConfig(
                    groupArtifact = "com.google.googlejavaformat:google-java-format",
                    reflowLongStrings = true,
                    reorderImports = true,
                    formatJavadoc = false,
                ),
                formatAnnotations = FormatAnnotationsConfig(),
                cleanthat = CleanthatJavaConfig(),
                toggleIfOffOn = true,
            ),
            KotlinExtension.NAME to FormatSettings(
                ktlint = KtlintConfig(
                    libs.versions.ktlint.get(),
                    "../.editorconfig",

                    ),
                target = listOf("**/*.kt"),
                trimTrailingWhitespace = true,
                endWithNewline = true,
                licenseHeader = LicenseHeaderConfig(
                    headerFile = LICENSE_HEADER_DIR,
                    delimiter = KotlinConstants.LICENSE_HEADER_DELIMITER,
                ),
                importOrder = ImportOrderConfig(),
                removeIfUnusedImports = true,
                toggleIfOffOn = true,
            ),
        ) + listOf(
            Format(
                "kts",
                listOf("kts"),
                KotlinConstants.LICENSE_HEADER_DELIMITER,
            ),
            Format(
                JsonExtension.NAME,
                listOf("json"),
            ),
            Format(
                "xml",
                listOf("xml"),
                "<(\\?xml)?",
            ),
            Format(
                YamlExtension.NAME,
                listOf("yaml", "yml"),
            ),
            Format(
                "properties",
                listOf("properties"),
            ),
            Format(
                "html",
                listOf("html"),
                "<(!DOCTYPE |html )",
            ),
            Format(
                JavascriptExtension.NAME,
                listOf("js"),
            ),
            Format(
                TypescriptExtension.NAME,
                listOf("ts"),
            ),
            Format(
                "md",
                listOf("md"),
            ),
            Format(
                "gitignore",
                listOf("gitignore"),
            ),
            Format(
                "gitattributes",
                listOf("gitattributes"),
            ),
        ).associate { (name, extensions, delimiter) ->
            name to FormatSettings(
                trimTrailingWhitespace = true,
                endWithNewline = true,
                licenseHeader = LicenseHeaderConfig(
                    headerFile = LICENSE_HEADER_DIR,
                    delimiter = delimiter,
                ),
                target = extensions.map { extension -> "**/*.$extension" },
                toggleIfOffOn = true,
            )
        }


        lineEndings?.let(extension::setLineEndings)
        encoding?.let(extension::setEncoding)
        ratchetFrom?.let(extension::setRatchetFrom)
        enforceCheck?.let(extension::setEnforceCheck)

        // Applicable only in root project.
        if (project == project.rootProject) {
            predeclareDeps?.takeIf { it }.run { extension.predeclareDeps() }
            predeclareDepsFromBuildscript?.takeIf { it }.run { extension.predeclareDepsFromBuildscript() }
        }

        // Format files
        formats.ifEmpty { null }?.forEach { (name, settings) ->
            extension.format(name, settings::applyTo)
        }

        kotlinGradle?.let { kotlinGradle ->
            // Additional configuration for Kotlin Gradle scripts
            extension.kotlinGradle(kotlinGradle::applyTo)
        }
    }
}

internal data class Format(
    val name: String,
    val extensions: List<String>,
    val delimiter: String = "^",
)
