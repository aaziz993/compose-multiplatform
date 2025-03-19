package gradle.plugins.kmp

internal interface HasBinaries<out T> {

    val binaries: T
}
