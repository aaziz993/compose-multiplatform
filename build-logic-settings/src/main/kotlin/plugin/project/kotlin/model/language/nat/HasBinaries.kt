package plugin.project.kotlin.model.language.nat

internal interface HasBinaries<out T : Set<*>> {
    val binaries: T
}
