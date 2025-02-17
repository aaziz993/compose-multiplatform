package plugin.project.kotlin.atomicfu.model

internal interface AtomicFUExtension {

    val dependenciesVersion: String?
    val transformJvm: Boolean?
    val jvmVariant: String?
    val verbose: Boolean?
}
