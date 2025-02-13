package plugin.project.amperlike

import org.jetbrains.amper.frontend.FragmentDependencyType
import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.PlatformAware
import plugin.project.amperlike.model.AmperLikeArtifact
import plugin.project.amperlike.model.AmperLikeModule

internal data class AmperLikeModuleWrapper(
    private val passedModule: AmperLikeModule
): AmperLikeModule by passedModule{
    // Wrapper functions.
    private val allFragmentWrappers = mutableMapOf<AmperLikeFragment, AmperLikeFragmentWrapper>()
    val AmperLikeLeafFragment.wrappedLeaf get() = wrapped as AmperLikeLeafFragmentWrapper
    val AmperLikeFragment.wrapped
        get() = this as? AmperLikeFragmentWrapper
            ?: allFragmentWrappers.computeIfAbsent(this) {
                if (this is AmperLikeLeafFragment) AmperLikeLeafFragmentWrapper(this@AmperLikeModuleWrapper, this)
                else AmperLikeFragmentWrapper(this@AmperLikeModuleWrapper, this)
            }

    val artifactPlatforms by lazy { artifacts.flatMap { it.platforms }.toSet() }
        val fragmentsByName by lazy { fragments.associateBy { it.name } }
    override val artifacts = passedModule.artifacts.map { it.wrap(this) }
    override val fragments = passedModule.fragments.map { it.wrapped }
    override val leafFragments = passedModule.fragments.filterIsInstance<AmperLikeLeafFragment>().map { it.wrappedLeaf }
    override val rootFragment = passedModule.rootFragment.wrapped
    override val rootTestFragment = passedModule.rootTestFragment.wrapped
    val leafNonTestFragments = leafFragments
        .filter { !it.isTest }
    val leafTestFragments = leafFragments
        .filter { it.isTest }

    fun sharedPlatformFragment(platform: Platform, test: Boolean): AmperLikeFragmentWrapper? {
        // find the most common fragment
        val commonFragment = passedModule.fragments.firstOrNull { it.fragmentDependencies.isEmpty() } ?: return null

        // dfs
        val queue = ArrayDeque<AmperLikeFragment>()
        queue.add(commonFragment)
        while (queue.isNotEmpty()) {
            val fragment = queue.removeFirst()
            if (fragment.platforms == setOf(platform) && fragment.isTest == test) {
                return fragment.wrapped
            }

            fragment.fragmentDependants.forEach {
                queue.add(it.target)
            }
        }

        return null
    }
}


internal fun AmperLikeArtifact.wrap(module: AmperLikeModuleWrapper) =
    AmperLikeArtifactWrapper(this, module)

internal open class AmperLikeArtifactWrapper(
    artifact: AmperLikeArtifact,
    private val module: AmperLikeModuleWrapper,
) : AmperLikeArtifact by artifact, PlatformAware {
    override val fragments = artifact.fragments.map { with(module) { it.wrappedLeaf } }
}

internal open class AmperLikeFragmentWrapper(
    override val module: AmperLikeModuleWrapper,
    private val fragment: AmperLikeFragment
) : AmperLikeFragment by fragment, PlatformAware {
    override fun toString(): String = "FragmentWrapper(fragment=${fragment.name})"

    val refineDependencies by lazy {
        with(module) {
            fragmentDependencies.filter { it.type == FragmentDependencyType.REFINE }.map { it.target.wrapped }
        }
    }
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
internal class AmperLikeLeafFragmentWrapper(
    override val module: AmperLikeModuleWrapper,
    fragment: AmperLikeLeafFragment,
) : AmperLikeFragmentWrapper(module, fragment), AmperLikeLeafFragment by fragment, PlatformAware
