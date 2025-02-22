package plugin.project.java.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.jvm.toolchain.internal.DefaultJvmVendorSpec
import plugin.project.java.configureJar

@Serializable
internal data class JavaSettings(
    override val sourceCompatibility: JavaVersion? = null,
    override val targetCompatibility: JavaVersion? = null,
    override val disableAutoTargetJvm: Boolean? = null,
    override val withJavadocJar: Boolean? = null,
    override val withSourcesJar: Boolean? = null,
    override val modularity: ModularitySpec? = null,
    override val toolchain: JavaToolchainSpec? = null,
    override val consistentResolution: JavaResolutionConsistency? = null,
    override val docsDir: String? = null,
    override val testResultsDir: String? = null,
    override val testReportDir: String? = null,
    override val manifest: Manifest? = null,
    val application: JavaApplication? = null,
    val jar: Jar? = null,
) : JavaPluginExtension {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(extension: org.gradle.api.plugins.JavaPluginExtension) {
        sourceCompatibility?.let(extension::setSourceCompatibility)
        targetCompatibility?.let(extension::setTargetCompatibility)
        disableAutoTargetJvm?.takeIf { it }?.run { extension.disableAutoTargetJvm() }
        withJavadocJar?.takeIf { it }?.run { extension.withJavadocJar() }
        withSourcesJar?.takeIf { it }?.run { extension.withSourcesJar() }

        modularity?.let { modularity ->
            extension.modularity.inferModulePath tryAssign modularity.inferModulePath
        }

        toolchain?.let { toolchain ->
            extension.toolchain {
                languageVersion tryAssign toolchain.languageVersion
                toolchain.vendor?.let { _vendor ->
                    vendor tryAssign _vendor.matches?.let(DefaultJvmVendorSpec::matching)
                }
            }
        }

        consistentResolution?.let { consistentResolution ->
            extension.consistentResolution {
                consistentResolution.useCompileClasspathVersions?.takeIf { it }?.run { useCompileClasspathVersions() }
                consistentResolution.useRuntimeClasspathVersions?.takeIf { it }?.run { useRuntimeClasspathVersions() }
            }
        }

        extension.docsDir tryAssign docsDir?.let(layout.projectDirectory::dir)
        extension.testResultsDir tryAssign testResultsDir?.let(layout.projectDirectory::dir)
        extension.testReportDir tryAssign testReportDir?.let(layout.projectDirectory::dir)

        manifest?.let { manifest ->
            extension.manifest {
                manifest.attributes?.let(attributes::putAll)
            }
        }


    }
}
