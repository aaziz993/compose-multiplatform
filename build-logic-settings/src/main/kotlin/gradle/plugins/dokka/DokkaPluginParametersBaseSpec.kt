package gradle.plugins.dokka

import gradle.accessors.libraryAsDependency
import gradle.accessors.libs
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.BaseNamed
import gradle.api.tryAssign
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.kotlin.dsl.buildscript
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import org.jetbrains.dokka.gradle.engine.plugins.DokkaVersioningPluginParameters

/**
 * Base class for defining Dokka Plugin configuration.
 *
 * This class should not be instantiated directly.
 * Instead, define a subclass that implements the [jsonEncode] function.
 *
 * @param[name] A descriptive name of the item in the [org.jetbrains.dokka.gradle.internal.DokkaPluginParametersContainer].
 * The name is only used for identification in the Gradle buildscripts.
 * @param[pluginFqn] Fully qualified classname of the Dokka Plugin
 */
@Serializable
internal sealed class DokkaPluginParametersBaseSpec : BaseNamed {

    abstract val pluginFqn: String
}

internal object DokkaPluginParametersBaseSpecTransformingSerializer : KeyTransformingSerializer<DokkaPluginParametersBaseSpec>(
    DokkaPluginParametersBaseSpec.serializer(),
    "type",
)

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
    val customAssets: List<String>? = null,

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
    val customStyleSheets: List<String>? = null,

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
) : DokkaPluginParametersBaseSpec() {

    override val name: String
        get() = DokkaHtmlPluginParameters.DOKKA_HTML_PARAMETERS_NAME

    override val pluginFqn: String
        get() = DokkaHtmlPluginParameters.DOKKA_HTML_PLUGIN_FQN

        context(Project)
    override fun applyTo(recipient: T) {
        named as DokkaHtmlPluginParameters

        customAssets?.let(named.customAssets::setFrom)
        customStyleSheets?.let(named.customStyleSheets::setFrom)
        named.separateInheritedMembers tryAssign separateInheritedMembers
        named.mergeImplicitExpectActualDeclarations tryAssign mergeImplicitExpectActualDeclarations
        named.footerMessage tryAssign (footerMessage ?: listOfNotNull(
            projectProperties.year,
            projectProperties.developer?.name,
        ).joinToString(" - ").takeIf(String::isNotEmpty)?.let { message -> "© $message" })
        named.homepageLink tryAssign homepageLink
        named.templatesDir tryAssign templatesDir?.let(layout.projectDirectory::dir)
    }
}

/**
 * Configuration for
 * [Dokka's Versioning plugin](https://github.com/Kotlin/dokka/tree/master/plugins/versioning#readme).
 *
 * The versioning plugin provides the ability to host documentation for multiple versions of your
 * library/application with seamless switching between them. This, in turn, provides a better
 * experience for your users.
 *
 * Note: The versioning plugin only works with Dokka's HTML format.
 */
@Serializable
@SerialName("versioning")
internal data class DokkaVersioningParameters(

    /**
     * The version of your application/library that documentation is going to be generated for.
     * This will be the version shown in the dropdown menu.
     */
    val version: String? = null,

    /**
     * An optional list of strings that represents the order that versions should appear in the
     * dropdown menu.
     *
     * Must match [version] string exactly. The first item in the list is at the top of the dropdown.
     * Any versions not in this list will be excluded from the dropdown.
     *
     * If no versions are supplied the versions will be ordered using SemVer ordering.
     */
    val versionsOrdering: List<String>? = null,

    /**
     * An optional path to a parent folder that contains other documentation versions.
     * It requires a specific directory structure.
     *
     * For more information, see
     * [Directory structure](https://github.com/Kotlin/dokka/blob/master/plugins/versioning/README.md#directory-structure).
     */
    val olderVersionsDir: String? = null,

    /**
     * An optional list of paths to other documentation versions. It must point to Dokka's outputs
     * directly. This is useful if different versions can't all be in the same directory.
     */
    val olderVersions: List<String>? = null,

    /**
     * An optional boolean value indicating whether to render the navigation dropdown on all pages.
     *
     * Set to `true` by default.
     */
    val renderVersionsNavigationOnAllPages: Boolean? = null,
) : DokkaPluginParametersBaseSpec() {

    override val name: String
        get() = DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_PARAMETERS_NAME

    override val pluginFqn: String
        get() = DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_FQN

        context(Project)
    override fun applyTo(recipient: T) {
        buildscript {
            dependencies {
                classpath(settings.libs.libraryAsDependency("dokka.versioning"))
            }
        }

        named as DokkaVersioningPluginParameters

        named.olderVersionsDir tryAssign olderVersionsDir?.let(::file)
        olderVersions?.let(named.olderVersions::setFrom)
        named.versionsOrdering tryAssign versionsOrdering
        named.version tryAssign version
        named.renderVersionsNavigationOnAllPages tryAssign renderVersionsNavigationOnAllPages
    }
}

