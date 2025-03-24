package plugins.apivalidation.model

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.accessors.apiBuild
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.apivalidation.ApiValidationExtension
import gradle.plugins.apivalidation.KlibValidationSettings
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

@Serializable
internal data class ApiValidationSettings(
    override val validationDisabled: Boolean? = null,
    override val ignoredPackages: Set<String>? = null,
    override val setIgnoredPackages: Set<String>? = null,
    override val ignoredProjects: Set<String>? = null,
    override val setIgnoredProjects: Set<String>? = null,
    override val nonPublicMarkers: Set<String>? = null,
    override val setNonPublicMarkers: Set<String>? = null,
    override val ignoredClasses: Set<String>? = null,
    override val setIgnoredClasses: Set<String>? = null,
    override val publicMarkers: Set<String>? = null,
    override val setPublicMarkers: Set<String>? = null,
    override val publicPackages: Set<String>? = null,
    override val setPublicPackages: Set<String>? = null,
    override val publicClasses: Set<String>? = null,
    override val setPublicClasses: Set<String>? = null,
    override val additionalSourceSets: Set<String>? = null,
    override val setAdditionalSourceSets: Set<String>? = null,
    override val apiDumpDirectory: String? = null,
    override val klib: KlibValidationSettings? = null,
    override val enabled: Boolean = true
) : ApiValidationExtension, EnabledSettings {

    context(project: Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("binaryCompatibilityValidator").id) {
            super.applyTo()

            project.tasks.apiBuild {
                // "jar" here is the name of the default Jar task producing the resulting jar file
                // in a multiplatform project it can be named "jvmJar"
                // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
                inputJar.value(project.tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
            }
        }
}
