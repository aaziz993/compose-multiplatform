package plugin.project.kotlin.model

internal interface HasBinaries<out T> {
    val binaries: T
}
