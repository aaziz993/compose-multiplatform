@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.gradle.spotless

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.gradle.spotless.JavascriptExtension
import com.diffplug.gradle.spotless.JsonExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.TypescriptExtension
import com.diffplug.gradle.spotless.YamlExtension
import com.diffplug.spotless.generic.LicenseHeaderStep
import com.diffplug.spotless.kotlin.KotlinConstants
import com.diffplug.spotless.kotlin.KtfmtStep
import gradle.amperModuleExtraProperties
import gradle.libs
import org.gradle.api.Project
import plugin.project.gradle.spotless.model.BaseKotlinExtension
import plugin.project.gradle.spotless.model.CleanthatJavaConfig
import plugin.project.gradle.spotless.model.FormatAnnotationsConfig
import plugin.project.gradle.spotless.model.FormatSettings
import plugin.project.gradle.spotless.model.GoogleJavaFormatConfig
import plugin.project.gradle.spotless.model.ImportOrderConfig
import plugin.project.gradle.spotless.model.KtlintConfig
import plugin.project.gradle.spotless.model.LicenseHeaderConfig

private const val LICENSE_HEADER_DIR = "../"

internal fun Project.configureSpotlessExtension(extension: SpotlessExtension) =
    amperModuleExtraProperties.settings.gradle.spotless.let { spotless ->
        extension.apply {
            val targetExclude = listOf(
                "**/generated-src/**",
                "**/build-*/**",
                "**/${layout.buildDirectory.get()}/**",
                "**/.idea/**",
                "**/.fleet/**",
                "**/.idea/**",
                "**/.gradle/**",
                "/spotless/**",
                "**/resources/**",
                "**/buildSrc/**",
            )

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


            spotless.lineEndings?.let(::setLineEndings)
            spotless.encoding?.let(::setEncoding)
            spotless.ratchetFrom?.let(::setRatchetFrom)
            spotless.enforceCheck?.let(::setEnforceCheck)
            spotless.predeclareDeps?.takeIf { it }.run { predeclareDeps() }
            spotless.predeclareDepsFromBuildscript?.takeIf { it }.run { predeclareDepsFromBuildscript() }

            // Format files
            (spotless.formats?.ifEmpty { null }
                ?: formats).filterValues(FormatSettings::enabled).forEach { (name, settings) ->
                format(name) {
                    settings.lineEnding?.let(::setLineEndings)
                    settings.ratchetFrom?.let(::setRatchetFrom)
                    settings.excludeSteps?.forEach(::ignoreErrorForStep)
                    settings.excludePaths?.forEach(::ignoreErrorForPath)
                    settings.encoding?.let(::setEncoding)
                    settings.target?.let { target(*it.toTypedArray()) }
                    targetExclude(
                        *(settings.targetExclude.orEmpty() + targetExclude).toTypedArray(),
                    )
                    settings.targetExcludeIfContentContains?.let(::targetExcludeIfContentContains)
                    settings.targetExcludeIfContentContainsRegex?.let(::targetExcludeIfContentContainsRegex)
                    settings.replace?.forEach { (name, original, replacement) -> replace(name, original, replacement) }
                    settings.replaceRegex?.forEach { (name, regex, replacement) -> replaceRegex(name, regex, replacement) }
                    settings.trimTrailingWhitespace?.takeIf { it }?.run { trimTrailingWhitespace() }
                    settings.endWithNewline?.takeIf { it }?.run { endWithNewline() }
                    settings.indentWithSpaces?.let(::indentWithSpaces)
                    settings.indentIfWithSpaces?.takeIf { it }?.let { trimTrailingWhitespace() }
                    settings.indentWithTabs?.let(::indentWithTabs)
                    settings.indentIfWithTabs?.takeIf { it }?.run { indentWithTabs() }
                    settings.nativeCmd?.forEach { (name, pathToExe, arguments) -> nativeCmd(name, pathToExe, arguments) }
                    settings.licenseHeader?.let { license ->
                        (license.header?.let { licenseHeader(it, license.delimiter) }
                            ?: licenseHeader(license.headerFile!!, license.delimiter))
                            .apply {
                                license.name?.let(::named)
                                license.contentPattern?.let(::onlyIfContentMatches)
                                license.yearSeparator?.let(::yearSeparator)
                                license.skipLinesMatching?.let(::skipLinesMatching)
                                license.updateYearWithLatest?.let(::updateYearWithLatest)
                            }
                    }
                    settings.prettier?.let { prettier ->
                        prettier(prettier.devDependencies)
                    }
                    settings.clangFormat?.let { clangFormat ->
                        clangFormat().apply {
                            clangFormat.version?.let(::setVersion)
                            clangFormat.pathToExe?.let(::pathToExe)
                            clangFormat.style?.let(::style)
                        }
                    }

                    if (this is JavaExtension) {
                        settings.importOrder?.let { importOrder ->
                            importOrder().apply {
                                importOrder.importOrder?.let { importOrder(*it.toTypedArray()) }
                                importOrder.importOrderFile?.let(::importOrderFile)
                                importOrder.wildcardsLast?.let(::wildcardsLast)
                                importOrder.semanticSort?.let(::semanticSort)
                                importOrder.treatAsPackage?.let(::treatAsPackage)
                                importOrder.treatAsClass?.let(::treatAsClass)
                            }
                        }
                        settings.removeIfUnusedImports?.let { removeUnusedImports() }
                        settings.removeUnusedImports?.let(::removeUnusedImports)
                        settings.googleJavaFormat?.let { googleJavaFormat ->
                            googleJavaFormat().apply {
                                googleJavaFormat.version?.let(::setVersion)
                                googleJavaFormat.groupArtifact?.let(::groupArtifact)
                                googleJavaFormat.style?.let(::style)
                                googleJavaFormat.reflowLongStrings?.let(::reflowLongStrings)
                                googleJavaFormat.reorderImports?.let(::reorderImports)
                                googleJavaFormat.formatJavadoc?.let(::formatJavadoc)
                            }
                        }
                        settings.palantirJavaFormat?.let { palantirJavaFormat ->
                            palantirJavaFormat().apply {
                                palantirJavaFormat.version?.let(::setVersion)
                                palantirJavaFormat.style?.let(::style)
                                palantirJavaFormat.formatJavadoc?.let(::formatJavadoc)
                            }
                        }
                        settings.eclipse?.let { eclipse ->
                            (eclipse.formatterVersion?.let(::eclipse) ?: eclipse()).apply {
                                eclipse.settingsFiles?.let { configFile(*it.toTypedArray()) }
                                eclipse.p2Mirrors?.let(::withP2Mirrors)
                            }
                        }
                        settings.formatAnnotations?.let { formatAnnotations ->
                            formatAnnotations().apply {
                                formatAnnotations.addedTypeAnnotations?.forEach(::addTypeAnnotation)
                                formatAnnotations.removedTypeAnnotations?.forEach(::removeTypeAnnotation)
                            }
                        }
                        settings.cleanthat?.let { cleanthat ->
                            cleanthat().apply {
                                cleanthat.groupArtifact?.let(::groupArtifact)
                                cleanthat.version?.let(::version)
                                cleanthat.sourceJdk?.let(::sourceCompatibility)
                                cleanthat.mutators?.let(::addMutators)
                                cleanthat.excludedMutators?.forEach(::excludeMutator)
                                cleanthat.includeDraft?.let(::includeDraft)
                            }
                        }
                    }

                    (this as? KotlinExtension)?.configurFrom(settings)

                    settings.eclipseWtp?.let { eclipseWtp ->
                        (eclipseWtp.version?.let { eclipseWtp(eclipseWtp.type, it) }
                            ?: eclipseWtp(eclipseWtp.type)).apply {
                            eclipseWtp.settingsFiles?.let { configFile(*it.toTypedArray()) }
                        }
                    }
                    settings.toggleOffOnRegex?.let(::toggleOffOnRegex)
                    settings.toggleOffOn?.let { (off, on) -> toggleOffOn(off, on) }
                    settings.toggleIfOffOn?.takeIf { it }.run { toggleOffOn() }
                    settings.toggleOffOnDisable?.takeIf { it }.run { toggleOffOnDisable() }
                }
            }

            spotless.kotlinGradle?.let { kotlinGradle ->
                // Additional configuration for Kotlin Gradle scripts
                kotlinGradle {
                    configurFrom(kotlinGradle)
                }
            }
        }
    }

private fun com.diffplug.gradle.spotless.BaseKotlinExtension.configurFrom(settings: BaseKotlinExtension) {
    settings.diktat?.let { diktat ->
        (diktat.version?.let(::diktat) ?: diktat()).apply {

            diktat.config?.let(::configFile)
        }
    }
    settings.ktfmt?.forEach { ktfmt ->
        (ktfmt.version?.let(::ktfmt) ?: ktfmt()).apply {
            when (ktfmt.style) {
                KtfmtStep.Style.DROPBOX -> dropboxStyle()
                KtfmtStep.Style.GOOGLE -> googleStyle()
                KtfmtStep.Style.KOTLINLANG -> kotlinlangStyle()
                else -> throw IllegalArgumentException("Unsupported ktfmt default style")
            }.configure { options ->
                ktfmt.options.maxWidth?.let(options::setMaxWidth)
                ktfmt.options.blockIndent?.let(options::setBlockIndent)
                ktfmt.options.continuationIndent?.let(options::setContinuationIndent)
                ktfmt.options.removeUnusedImport?.let(options::setRemoveUnusedImport)
            }
        }
    }
    settings.ktlint?.let { ktlint ->
        (ktlint.version?.let(::ktlint) ?: ktlint()).apply {
            ktlint.editorConfigPath?.let(::setEditorConfigPath)
            ktlint.editorConfigOverride?.let(::editorConfigOverride)
            ktlint.customRuleSets?.let(::customRuleSets)
        }
    }
}

internal data class Format(
    val name: String,
    val extensions: List<String>,
    val delimiter: String = "^",
)



