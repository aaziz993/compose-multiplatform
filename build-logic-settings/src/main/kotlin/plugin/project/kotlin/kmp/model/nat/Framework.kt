package plugin.project.kotlin.kmp.model.nat

import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

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
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as Framework

        named::isStatic trySet this@Framework.isStatic
    }
}
