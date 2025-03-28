package gradle.plugins.dokka.plugin

import gradle.accessors.libraryAsDependency
import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.api.tryFrom
import gradle.api.trySetFrom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.buildscript
import org.jetbrains.dokka.gradle.engine.plugins.DokkaVersioningPluginParameters

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
internal data class DokkaVersioningPluginParameters(

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
    val versionsOrdering: LinkedHashSet<String>? = null,

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
    val olderVersions: LinkedHashSet<String>? = null,
    val setOlderVersions: LinkedHashSet<String>? = null,

    /**
     * An optional boolean value indicating whether to render the navigation dropdown on all pages.
     *
     * Set to `true` by default.
     */
    val renderVersionsNavigationOnAllPages: Boolean? = null,
) : DokkaPluginParametersBaseSpec<DokkaVersioningPluginParameters>() {

    override val name: String
        get() = DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_PARAMETERS_NAME

    override val pluginFqn: String
        get() = DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_FQN

    context(Project)
    override fun applyTo(receiver: DokkaVersioningPluginParameters) {
        buildscript {
            dependencies {
                classpath(project.settings.libs.libraryAsDependency("dokka.versioning"))
            }
        }

        receiver.olderVersionsDir tryAssign olderVersionsDir?.let(project::file)
        receiver.olderVersions tryFrom olderVersions
        receiver.olderVersions trySetFrom setOlderVersions
        receiver.versionsOrdering tryAssign versionsOrdering
        receiver.version tryAssign version
        receiver.renderVersionsNavigationOnAllPages tryAssign renderVersionsNavigationOnAllPages
    }
}

