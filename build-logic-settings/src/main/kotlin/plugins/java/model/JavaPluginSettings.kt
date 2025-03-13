package plugins.java.model

import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.JavaResolutionConsistency
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.java.Manifest
import gradle.plugins.java.ModularitySpec
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.JavaVersion

@Serializable
internal data class JavaPluginSettings(
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
    override val enabled: Boolean = true
) : JavaPluginExtension, EnabledSettings
