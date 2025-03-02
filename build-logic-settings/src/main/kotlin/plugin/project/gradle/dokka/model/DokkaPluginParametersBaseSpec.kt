package plugin.project.gradle.dokka.model

import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.tryAssign
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import plugin.project.kotlin.model.Named

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
internal sealed class DokkaPluginParametersBaseSpec : Named {

    abstract val pluginFqn: String

    context(Project)
    abstract fun applyTo(spec: org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec)
}

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
    override fun applyTo(spec: org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec) {
        spec as DokkaHtmlPluginParameters

        customAssets?.let(spec.customAssets::setFrom)
        customStyleSheets?.let(spec.customStyleSheets::setFrom)
        spec.separateInheritedMembers tryAssign separateInheritedMembers
        spec.mergeImplicitExpectActualDeclarations tryAssign mergeImplicitExpectActualDeclarations
        spec.footerMessage tryAssign footerMessage
        spec.homepageLink tryAssign homepageLink
        spec.templatesDir tryAssign templatesDir?.let(layout.projectDirectory::dir)
    }
}

internal object DokkaPluginParametersBaseSpecTransformingSerializer : KeyTransformingSerializer<DokkaPluginParametersBaseSpec>(
    DokkaPluginParametersBaseSpec.serializer(),
    "type",
)

