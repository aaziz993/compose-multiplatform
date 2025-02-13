package plugin.project.amperlike.model

import org.jetbrains.amper.frontend.Platform
import plugin.project.amperlike.AmperLikeLeafFragment

internal interface AmperLikeArtifact {

    val name: String
    val fragments: List<AmperLikeLeafFragment>
    val platforms: Set<Platform>
    val isTest: Boolean
}
