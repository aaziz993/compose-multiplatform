package plugin.project.kotlin.kmp.model.nat

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import plugin.project.kotlin.model.configure

@Serializable
internal data class Framework(
    override val name: String = "",
    override val baseName: String? = null,
    override val transitiveExport: Boolean? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val isStatic: Boolean? = null,
    val buildTypes: List<NativeBuildType>? = null,
) : AbstractNativeLibrary {

    context(Project)
    override fun applyTo(binaries: NamedDomainObjectCollection<out NativeBinary>) {
        super.applyTo(binaries)

        binaries.configure {
            this as Framework

            ::isStatic trySet this@Framework.isStatic
        }
    }
}
