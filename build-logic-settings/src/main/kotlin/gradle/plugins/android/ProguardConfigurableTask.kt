package gradle.plugins.android

import com.android.build.gradle.internal.tasks.ProguardConfigurableTask

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Base class for tasks that consume ProGuard configuration files.
 *
 * We use this type to configure ProGuard and the R8 consistently, using the same
 * code.
 */
@Serializable
internal data class ProguardConfigurableTask(
    override val projectPath: String? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val variantName: String? = null,
//    val componentType: ComponentType? = null,
    val includeFeaturesInScopes: Boolean? = null,
    val testedMappingFile: List<String>? = null,
    val classes: List<String>? = null,
    val resourcesJar: String? = null,
    val referencedClasses: List<String>? = null,
    val referencedResources: List<String>? = null,
    val extractedDefaultProguardFile: String? = null,
    val generatedProguardFile: List<String>? = null,
    val configurationFiles: List<String>? = null,
    val libraryKeepRulesFileCollection: List<String>? = null,
    val ignoreFromInKeepRules: Set<String>? = null,
    val ignoreFromAllExternalDependenciesInKeepRules: Boolean? = null,
    val mappingFile: String? = null,
    val hasAllAccessTransformers: Boolean? = null,
) : NonIncrementalTask() {

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as ProguardConfigurableTask

        named.includeFeaturesInScopes tryAssign includeFeaturesInScopes
        testedMappingFile?.let(named.testedMappingFile::setFrom)
        classes?.let(named.classes::setFrom)
        named.resourcesJar tryAssign resourcesJar?.let(::file)
        referencedClasses?.let(named.referencedClasses::setFrom)
        referencedResources?.let(named.referencedResources::setFrom)
        named.extractedDefaultProguardFile tryAssign extractedDefaultProguardFile?.let(project.layout.projectDirectory::dir)
        generatedProguardFile?.let(named.generatedProguardFile::setFrom)
        configurationFiles?.let(named.configurationFiles::setFrom)
        libraryKeepRulesFileCollection?.let(named.libraryKeepRulesFileCollection::setFrom)
        named.ignoreFromInKeepRules tryAssign ignoreFromInKeepRules
        named.ignoreFromAllExternalDependenciesInKeepRules tryAssign ignoreFromAllExternalDependenciesInKeepRules
        named.mappingFile tryAssign mappingFile?.let(::file)
        named.hasAllAccessTransformers tryAssign hasAllAccessTransformers
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ProguardConfigurableTask>())
}
