package gradle.plugins.animalsniffer.model

import gradle.plugins.animalsniffer.AnimalSnifferExtension
import gradle.plugins.animalsniffer.CheckCacheExtension
import gradle.api.EnabledSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimalSnifferSettings(
    override val debug: Boolean? = null,
    override val annotation: String? = null,
    override val ignore: Set<String>? = null,
    override val setIgnore: Set<String>? = null,
    override val excludeJars: Set<String>? = null,
    override val setExcludeJars: Set<String>? = null,
    override val cache: CheckCacheExtension? = null,
    override val checkTestSources: Boolean? = null,
    override val defaultTargets: Set<String>? = null,
    override val setDefaultTargets: Set<String>? = null,
    override val failWithoutSignatures: Boolean? = null,
    override val toolVersion: String? = null,
    override val sourceSets: Set<String>? = null,
    override val ignoreFailures: Boolean? = null,
    override val reportsDir: String? = null,
    override val enabled: Boolean = true,
) : AnimalSnifferExtension(), EnabledSettings
