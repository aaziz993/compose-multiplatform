package gradle.plugins.kmp

internal interface HasBinaries<T : Set<*>> {

    val binaries: T?
}
