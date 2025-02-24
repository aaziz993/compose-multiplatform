package plugin.project.gradle.apivalidation.model

import gradle.apiValidation
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.maybeNamed
import kotlinx.serialization.Serializable
import kotlinx.validation.KotlinApiBuildTask
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.named
import plugin.project.model.EnabledSettings

@Serializable
internal data class ApiValidationSettings(
    override val validationDisabled: Boolean? = null,
    override val ignoredPackages: Set<String>? = null,
    override val ignoredProjects: Set<String>? = null,
    override val nonPublicMarkers: Set<String>? = null,
    override val ignoredClasses: Set<String>? = null,
    override val publicMarkers: Set<String>? = null,
    override val publicPackages: Set<String>? = null,
    override val publicClasses: Set<String>? = null,
    override val additionalSourceSets: Set<String>? = null,
    override val apiDumpDirectory: String? = null,
    override val klib: KlibValidationSettings? = null,
    override val enabled: Boolean = true
) : ApiValidationExtension, EnabledSettings {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("binary.compatibility.validator").id) {
            super.applyTo(apiValidation)

            tasks.maybeNamed("apiBuild") {
                this as KotlinApiBuildTask
                // "jar" here is the name of the default Jar task producing the resulting jar file
                // in a multiplatform project it can be named "jvmJar"
                // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
                inputJar.value(tasks.named<Jar>("shadowJar").flatMap { it.archiveFile })
            }
        }
}
