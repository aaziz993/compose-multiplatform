package gradle.plugins.kmp

internal interface HasBinaries<out T : Set<*>> {

    val binaries: T?
}
