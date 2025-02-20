package plugin.project.kotlinnative.model

internal interface AbstractNativeLibrary : NativeBinary {

    val transitiveExport: Boolean?
}
