package gradle.plugins.android.tasks

import com.android.build.gradle.internal.tasks.ProguardConfigurableTask
import com.android.builder.core.ComponentTypeImpl
import gradle.api.tasks.applyTo

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap

import kotlinx.serialization.Serializable
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
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = "",
    override val variantName: String? = null,
    val componentType: ComponentTypeImpl? = null,
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
) : NonIncrementalTask<ProguardConfigurableTask>() {

    context(Project)
    override fun applyTo(recipient: ProguardConfigurableTask) {
        super.applyTo(recipient)

        recipient.componentType tryAssign componentType
        recipient.includeFeaturesInScopes tryAssign includeFeaturesInScopes
        testedMappingFile?.toTypedArray()?.let(recipient.testedMappingFile::from)
setTestedMappingFile?.let(recipient.testedMappingFile::setFrom)
        classes?.toTypedArray()?.let(recipient.classes::from)
setClasses?.let(recipient.classes::setFrom)
        recipient.resourcesJar tryAssign resourcesJar?.let(::file)
        referencedClasses?.toTypedArray()?.let(recipient.referencedClasses::from)
setReferencedClasses?.let(recipient.referencedClasses::setFrom)
        referencedResources?.toTypedArray()?.let(recipient.referencedResources::from)
setReferencedResources?.let(recipient.referencedResources::setFrom)
        recipient.extractedDefaultProguardFile tryAssign extractedDefaultProguardFile?.let(project.layout.projectDirectory::dir)
        generatedProguardFile?.toTypedArray()?.let(recipient.generatedProguardFile::from)
setGeneratedProguardFile?.let(recipient.generatedProguardFile::setFrom)
        configurationFiles?.toTypedArray()?.let(recipient.configurationFiles::from)
setConfigurationFiles?.let(recipient.configurationFiles::setFrom)
        libraryKeepRulesFileCollection?.toTypedArray()?.let(recipient.libraryKeepRulesFileCollection::from)
setLibraryKeepRulesFileCollection?.let(recipient.libraryKeepRulesFileCollection::setFrom)
        recipient.ignoreFromInKeepRules tryAssign ignoreFromInKeepRules
        recipient.ignoreFromAllExternalDependenciesInKeepRules tryAssign ignoreFromAllExternalDependenciesInKeepRules
        recipient.mappingFile tryAssign mappingFile?.let(::file)
        recipient.hasAllAccessTransformers tryAssign hasAllAccessTransformers
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ProguardConfigurableTask>())
}
