package plugin.project.gradle.apivalidation.model

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.id
import gradle.libs
import gradle.maybeNamed
import gradle.model.gradle.apivalidation.ApiValidationExtension
import gradle.model.gradle.apivalidation.KlibValidationSettings
import gradle.model.project.EnabledSettings
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import kotlinx.validation.KotlinApiBuildTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

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
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("binary.compatibility.validator").id) {
            super.applyTo()

            tasks.maybeNamed("apiBuild", KotlinApiBuildTask::class.java) {
                // "jar" here is the name of the default Jar task producing the resulting jar file
                // in a multiplatform project it can be named "jvmJar"
                // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
                inputJar.value(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
            }
        }
}
