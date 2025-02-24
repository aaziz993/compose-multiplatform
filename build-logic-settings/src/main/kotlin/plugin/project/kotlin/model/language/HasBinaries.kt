package plugin.project.kotlin.model.language

internal interface HasBinaries<out T> {
    val binaries: T
}
