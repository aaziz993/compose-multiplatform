package plugin.project.amperlike

import java.nio.file.Path
import org.jetbrains.amper.frontend.DefaultScopedNotation
import org.jetbrains.amper.frontend.FragmentDependencyType
import org.jetbrains.amper.frontend.Notation
import org.jetbrains.amper.frontend.Platform
import plugin.project.model.Settings
import plugin.project.amperlike.model.AmperLikeModule

/**
 * Some part of the module that supports "single resolve context" invariant for
 * every source and resource file that is included.
 */
internal interface AmperLikeFragment {

    /**
     * The name of this fragment.
     */
    val name: String

    /**
     * The module this fragment belongs to.
     */
    val module: AmperLikeModule

    /**
     * Fragments (within the same module) that this fragment depends on.
     */
    val fragmentDependencies: List<AmperLikeFragmentLink>

    /**
     * Fragments (within the same module) that depend on this fragment.
     */
    val fragmentDependants: List<AmperLikeFragmentLink>

    /**
     * Dependencies of this fragment. They can be Maven modules or other source modules in the project.
     */
    val externalDependencies: List<Notation>

    /**
     * The settings of this fragment, including inherited settings from parent fragments.
     * For instance, the settings of the iosArm64 fragment contain merged settings from iosArm64, ios, native, and common.
     */
    val settings: Settings

    /**
     * Leaf platforms that this fragment is compiled to.
     */
    val platforms: Set<Platform>

    /**
     * Whether this fragment contains test-only sources.
     */
    val isTest: Boolean

    /**
     * Is this fragment is chosen by default when
     * no variants are specified?
     */
    val isDefault: Boolean

    /**
     * Path to the sources' directory.
     */
    val src: Path

    /**
     * Path to the resources' directory.
     */
    val resourcesPath: Path

    /**
     * Path to compose resources' directory.
     */
    val composeResourcesPath: Path

    /**
     * Whether any processable files are present in [composeResourcesPath].
     */
    val hasAnyComposeResources: Boolean

    /**
     * Paths to the generated source roots, relative to the build directory.
     */
    val generatedSrcRelativeDirs: List<Path>

    /**
     * Paths to the generated resource roots, relative to the build directory.
     */
    val generatedResourcesRelativeDirs: List<Path>

    /**
     * Paths to the generated classes roots, relative to the build directory.
     */
    val generatedClassesRelativeDirs: List<Path>

    val variants: List<String>
}

/**
 * Fragments (within the same module) that this fragment depends on with the FRIEND relationship.
 * Internal declarations from these fragments will be visible despite the independent compilation.
 */
internal val AmperLikeFragment.friends: List<AmperLikeFragment>
    get() = fragmentDependencies.filter { it.type == FragmentDependencyType.FRIEND }.map { it.target }

/**
 * Fragments (within the same module) that this fragment depends on with the REFINE relationship.
 * Expect declarations from these fragments will be visible despite a possible independent compilation (for instance,
 * this is useful for metadata compilation).
 */
internal val AmperLikeFragment.refinedFragments: List<AmperLikeFragment>
    get() = fragmentDependencies.filter { it.type == FragmentDependencyType.REFINE }.map { it.target }

private val AmperLikeFragment.directModuleCompileDependencies: List<AmperLikeModule>
    get() = externalDependencies.filterIsInstance<AmperLikeLocalModuleDependency>().filter { it.compile }.map { it.module }

/**
 * Source fragments (not Maven) that this fragment depends on with compile scope.
 *
 * This includes fragment dependencies from the same module, but also fragments from "external" source module
 * dependencies that support a superset of the platforms.
 *
 * For example, if the `nativeMain` fragment of Module A depends on Module B, [allSourceFragmentCompileDependencies]
 * contains the `commonMain` fragment of Module A, and the `nativeMain` and `commonMain` fragments of Module B.
 */
// TODO this will eventually be more complicated: we need to support other dimensions than target platforms
//  (some sort of attribute matching supporting variants and the likes).
//  It is worth sharing the complete algorithm with dependency resolution (the same thing will be implemented for
//  modules that are downloaded from Maven, using their metadata artifacts).
internal val AmperLikeFragment.allSourceFragmentCompileDependencies: List<AmperLikeFragment>
    get() {
        val fragmentsFromThisModule = fragmentDependencies.map { it.target }
        val fragmentsFromOtherModules = directModuleCompileDependencies.flatMap { module ->
            module.fragments.filter { it.platforms.containsAll(platforms) }
        }

        // FIXME include transitive exported module dependencies

        return fragmentsFromThisModule + fragmentsFromOtherModules
    }

/**
 * Leaf fragment must have only one platform.
 * Also, it should contain parts, that are specific
 * for concrete artifacts.
 *
 * Each result artifact is specified by single leaf fragment
 * (Except for KMP libraries).
 */
internal interface AmperLikeLeafFragment : AmperLikeFragment {

    val platform: Platform
}

internal interface AmperLikeLocalModuleDependency : DefaultScopedNotation {

    val module: AmperLikeModule
}

/**
 * Dependency between fragments.
 * Can differ by type (refines dependency, test on sources dependency, etc.).
 */
internal interface AmperLikeFragmentLink {

    val target: AmperLikeFragment
    val type: FragmentDependencyType
}
