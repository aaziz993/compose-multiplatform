package plugin.project.gradle.animalsniffer.model

import gradle.model.gradle.animalsniffer.AnimalSnifferExtension
import gradle.model.gradle.animalsniffer.CheckCacheExtension
import gradle.model.gradle.knit.KnitPluginExtension
import gradle.model.project.EnabledSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimalSnifferSettings(
    override val debug: Boolean? = null,
    override val annotation: String? = null,
    override val ignore: List<String>? = null,
    override val excludeJars: List<String>? = null,
    override val cache: CheckCacheExtension? = null,
    override val checkTestSources: Boolean? = null,
    override val defaultTargets: Set<String>? = null,
    override val failWithoutSignatures: Boolean? = null,
    override val toolVersion: String? = null,
    override val sourceSets: List<String>? = null,
    override val ignoreFailures: Boolean? = null,
    override val reportsDir: String? = null,
    override val enabled: Boolean = true,
) : AnimalSnifferExtension(), EnabledSettings
