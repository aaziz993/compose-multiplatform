package gradle.plugins.kotlin.mpp

internal interface HasBinaries<T : Set<*>> {

    val binaries: T?
}
