package plugin.project.gradle.model

internal interface HasBinaries<out T> {
    val binaries: T
}
