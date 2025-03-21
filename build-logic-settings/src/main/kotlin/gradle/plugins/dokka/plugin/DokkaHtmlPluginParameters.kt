package gradle.plugins.dokka.plugin

import gradle.accessors.projectProperties
import gradle.api.tryAssign
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters

/**
 * Configuration for Dokka's base HTML format
 *
 * [More information is available in the Dokka docs.](https://kotlinlang.org/docs/dokka-html.html#configuration)
 */
@Serializable
@SerialName("html")
internal data class DokkaHtmlPluginParameters(
    /**
     * List of paths for image assets to be bundled with documentation.
     * The image assets can have any file extension.
     *
     * For more information, see
     * [Customizing assets](https://kotlinlang.org/docs/dokka-html.html#customize-assets).
     *
     * Be aware that files will be copied as-is to a specific directory inside the assembled Dokka
     * publication. This means that any relative paths must be written in such a way that they will
     * work _after_ the files are moved into the publication.
     *
     * It's best to try and mirror Dokka's directory structure in the source files, which can help
     * IDE inspections.
     */
    val customAssets: Set<String>? = null,
    val setCustomAssets: Set<String>? = null,

    /**
     * List of paths for `.css` stylesheets to be bundled with documentation and used for rendering.
     *
     * For more information, see
     * [Customizing assets](https://kotlinlang.org/docs/dokka-html.html#customize-assets).
     *
     * Be aware that files will be copied as-is to a specific directory inside the assembled Dokka
     * publication. This means that any relative paths must be written in such a way that they will
     * work _after_ the files are moved into the publication.
     *
     * It's best to try and mirror Dokka's directory structure in the source files, which can help
     * IDE inspections.
     */
    val customStyleSheets: Set<String>? = null,
    val setCustomStyleSheets: Set<String>? = null,

    /**
     * This is a boolean option. If set to `true`, Dokka renders properties/functions and inherited
     * properties/inherited functions separately.
     *
     * This is disabled by default.
     */
    val separateInheritedMembers: Boolean? = null,

    /**
     * This is a boolean option. If set to `true`, Dokka merges declarations that are not declared as
     * [expect/actual](https://kotlinlang.org/docs/multiplatform-connect-to-apis.html), but have the
     * same fully qualified name. This can be useful for legacy codebases.
     *
     * This is disabled by default.
     */
    val mergeImplicitExpectActualDeclarations: Boolean? = null,

    /** The text displayed in the footer. */
    val footerMessage: String? = null,

    /**
     * Creates a link to the project's homepage URL in the HTML header.
     *
     * The homepage icon is also overrideable: pass in custom image named `homepage.svg` to
     * [customAssets] (square icons work best).
     */
    val homepageLink: String? = null,

    /**
     * Path to the directory containing custom HTML templates.
     *
     * For more information, see [Templates](https://kotlinlang.org/docs/dokka-html.html#templates).
     */
    val templatesDir: String? = null,
) : DokkaPluginParametersBaseSpec<DokkaHtmlPluginParameters>() {

    override val name: String
        get() = DokkaHtmlPluginParameters.DOKKA_HTML_PARAMETERS_NAME

    override val pluginFqn: String
        get() = DokkaHtmlPluginParameters.DOKKA_HTML_PLUGIN_FQN

    context(Project)
    override fun applyTo(recipient: DokkaHtmlPluginParameters) {
        customAssets?.toTypedArray()?.let(recipient.customAssets::from)
        setCustomAssets?.let(recipient.customAssets::setFrom)
        customStyleSheets?.toTypedArray()?.let(recipient.customStyleSheets::from)
        setCustomStyleSheets?.let(recipient.customStyleSheets::setFrom)
        recipient.separateInheritedMembers tryAssign separateInheritedMembers
        recipient.mergeImplicitExpectActualDeclarations tryAssign mergeImplicitExpectActualDeclarations
        recipient.footerMessage tryAssign (footerMessage ?: listOfNotNull(
            projectProperties.year,
            projectProperties.developer?.name,
        ).joinToString(" - ").takeIf(String::isNotEmpty)?.let { message -> "© $message" })
        recipient.homepageLink tryAssign homepageLink
        recipient.templatesDir tryAssign templatesDir?.let(layout.projectDirectory::dir)
    }
}
