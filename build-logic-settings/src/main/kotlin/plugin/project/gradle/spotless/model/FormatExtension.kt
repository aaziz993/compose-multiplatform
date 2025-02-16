package plugin.project.gradle.spotless.model

import com.diffplug.spotless.LineEnding

internal interface FormatExtension: BaseKotlinExtension {

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
}
