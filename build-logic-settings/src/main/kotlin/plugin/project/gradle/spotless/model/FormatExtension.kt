package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.spotless.LineEnding
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlin.collections.toTypedArray

internal interface FormatExtension : BaseKotlinExtension {

    val lineEnding: LineEnding?
    val ratchetFrom: String?
    val excludeSteps: MutableSet<String>?
    val excludePaths: MutableSet<String>?
    val encoding: String?
    val target: List<String>?
    val targetExclude: List<String>?
    val targetExcludeIfContentContains: String?
    val targetExcludeIfContentContainsRegex: String?
    val replace: List<Replace>?
    val replaceRegex: List<Replace>?

    /** Removes trailing whitespace.  */
    val trimTrailingWhitespace: Boolean?

    /** Ensures that files end with a single newline.  */
    val endWithNewline: Boolean?

    /** Ensures that the files are indented using spaces.  */
    val indentWithSpaces: Int?

    /** Ensures that the files are indented using spaces.  */
    val indentIfWithSpaces: Boolean?

    /** Ensures that the files are indented using tabs.  */
    val indentWithTabs: Int?

    /** Ensures that the files are indented using tabs.  */
    val indentIfWithTabs: Boolean?
    val nativeCmd: List<NativeCmd>?
    val licenseHeader: LicenseHeaderConfig?
    val prettier: PrettierConfig?
    val clangFormat: ClangFormatConfig?

    fun applyTo(extension: FormatExtension) {
        lineEnding?.let(extension::setLineEndings)
        ratchetFrom?.let(extension::setRatchetFrom)
        excludeSteps?.forEach(extension::ignoreErrorForStep)
        excludePaths?.forEach(extension::ignoreErrorForPath)
        encoding?.let(extension::setEncoding)
        target?.let { extension.target(*it.toTypedArray()) }
        extension.targetExclude(
            *(targetExclude.orEmpty() + targetExclude).toTypedArray(),
        )
        targetExcludeIfContentContains?.let(extension::targetExcludeIfContentContains)
        targetExcludeIfContentContainsRegex?.let(extension::targetExcludeIfContentContainsRegex)
        replace?.forEach { (name, original, replacement) -> extension.replace(name, original, replacement) }
        replaceRegex?.forEach { (name, regex, replacement) -> extension.replaceRegex(name, regex, replacement) }
        trimTrailingWhitespace?.takeIf { it }?.run { extension.trimTrailingWhitespace() }
        endWithNewline?.takeIf { it }?.run { extension.endWithNewline() }
        indentWithSpaces?.let(extension::indentWithSpaces)
        indentIfWithSpaces?.takeIf { it }?.let { extension.trimTrailingWhitespace() }
        indentWithTabs?.let(extension::indentWithTabs)
        indentIfWithTabs?.takeIf { it }?.run { extension.indentWithTabs() }
        nativeCmd?.forEach { (name, pathToExe, arguments) -> extension.nativeCmd(name, pathToExe, arguments) }
        licenseHeader?.let { license ->
            license.applyTo(
                license.header?.let { extension.licenseHeader(it, license.delimiter) }
                    ?: extension.licenseHeader(license.headerFile!!, license.delimiter),
            )
        }
        prettier?.let { prettier ->
            extension.prettier(prettier.devDependencies)
        }
        clangFormat?.let { clangFormat ->
            clangFormat.applyTo(clangFormat.version?.let(extension::clangFormat) ?: extension.clangFormat())
        }
    }
}
