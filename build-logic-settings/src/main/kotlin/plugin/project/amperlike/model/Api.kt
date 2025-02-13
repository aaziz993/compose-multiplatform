package plugin.project.amperlike.model

import java.nio.file.Path

internal interface AmperLikeModel {
    val projectRoot: Path
    val modules: List<AmperLikeModule>
}
