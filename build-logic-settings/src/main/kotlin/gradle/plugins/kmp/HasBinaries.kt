package gradle.plugins.kmp

internal interface HasBinaries<T> {

    val binaries: T?

    fun applyTo(receiver: T)
}
