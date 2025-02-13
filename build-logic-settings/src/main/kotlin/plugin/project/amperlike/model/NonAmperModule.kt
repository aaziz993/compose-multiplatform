package plugin.project.amperlike.model

import org.jetbrains.amper.core.UsedInIdePlugin
import org.jetbrains.amper.frontend.AmperModuleSource
import org.jetbrains.amper.frontend.ClassBasedSet
import org.jetbrains.amper.frontend.CustomTaskDescription
import org.jetbrains.amper.frontend.ModulePart
import org.jetbrains.amper.frontend.VersionCatalog
import plugin.project.amperlike.AmperLikeFragment
import plugin.project.amperlike.AmperLikeLeafFragment

internal interface AmperLikeModule {

    val userReadableName: String

    val type: AmperLikeProductType

    val source: AmperModuleSource

    /**
     * Original schema values, that this module came from.
     */
    val origin: AmperLikeModule

    val fragments: List<AmperLikeFragment>

    val artifacts: List<AmperLikeArtifact>

    val parts: ClassBasedSet<ModulePart<*>>

    @UsedInIdePlugin
    val usedCatalog: VersionCatalog?

    val leafFragments get() = fragments.filterIsInstance<AmperLikeLeafFragment>()

    val rootFragment: AmperLikeFragment get() = fragments.first { it.fragmentDependencies.isEmpty() }

    val rootTestFragment: AmperLikeFragment get() = fragments.first { it.isTest && it.fragmentDependencies.size == 1 }

    val customTasks: List<CustomTaskDescription>


}
