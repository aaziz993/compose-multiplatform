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
    override val name: String? = null,
    override val variantName: String? = null,
    val componentType: ComponentTypeImpl? = null,
    val includeFeaturesInScopes: Boolean? = null,
    val testedMappingFile: Set<String>? = null,
    val setTestedMappingFile: Set<String>? = null,
    val classes: Set<String>? = null,
    val setClasses: Set<String>? = null,
    val resourcesJar: String? = null,
    val referencedClasses: Set<String>? = null,
    val setReferencedClasses: Set<String>? = null,
    val referencedResources: Set<String>? = null,
    val setReferencedResources: Set<String>? = null,
    val extractedDefaultProguardFile: String? = null,
    val generatedProguardFile: Set<String>? = null,
    val setGeneratedProguardFile: Set<String>? = null,
    val configurationFiles: LinkedHashSet<String>? = null,
    val setConfigurationFiles: LinkedHashSet<String>? = null,
    val libraryKeepRulesFileCollection: Set<String>? = null,
    val setLibraryKeepRulesFileCollection: Set<String>? = null,
    val ignoreFromInKeepRules: Set<String>? = null,
    val setIgnoreFromInKeepRules: Set<String>? = null,
    val ignoreFromAllExternalDependenciesInKeepRules: Boolean? = null,
    val mappingFile: String? = null,
    val hasAllAccessTransformers: Boolean? = null,
) : NonIncrementalTask<ProguardConfigurableTask>() {

    context(Project)
    override fun applyTo(receiver: ProguardConfigurableTask) {
        super.applyTo(receiver)

        receiver.componentType tryAssign componentType
        receiver.includeFeaturesInScopes tryAssign includeFeaturesInScopes
        testedMappingFile?.toTypedArray()?.let(receiver.testedMappingFile::from)
        setTestedMappingFile?.let(receiver.testedMappingFile::setFrom)
        classes?.toTypedArray()?.let(receiver.classes::from)
        setClasses?.let(receiver.classes::setFrom)
        receiver.resourcesJar tryAssign resourcesJar?.let(project::file)
        referencedClasses?.toTypedArray()?.let(receiver.referencedClasses::from)
        setReferencedClasses?.let(receiver.referencedClasses::setFrom)
        referencedResources?.toTypedArray()?.let(receiver.referencedResources::from)
        setReferencedResources?.let(receiver.referencedResources::setFrom)
        receiver.extractedDefaultProguardFile tryAssign extractedDefaultProguardFile?.let(project.layout.projectDirectory::dir)
        generatedProguardFile?.toTypedArray()?.let(receiver.generatedProguardFile::from)
        setGeneratedProguardFile?.let(receiver.generatedProguardFile::setFrom)
        configurationFiles?.toTypedArray()?.let(receiver.configurationFiles::from)
        setConfigurationFiles?.let(receiver.configurationFiles::setFrom)
        libraryKeepRulesFileCollection?.toTypedArray()?.let(receiver.libraryKeepRulesFileCollection::from)
        setLibraryKeepRulesFileCollection?.let(receiver.libraryKeepRulesFileCollection::setFrom)

        receiver.ignoreFromInKeepRules tryAssign ignoreFromInKeepRules?.let { ignoreFromInKeepRules ->
            receiver.ignoreFromInKeepRules.get() + ignoreFromInKeepRules
        }

        receiver.ignoreFromInKeepRules tryAssign setIgnoreFromInKeepRules
        receiver.ignoreFromAllExternalDependenciesInKeepRules tryAssign ignoreFromAllExternalDependenciesInKeepRules
        receiver.mappingFile tryAssign mappingFile?.let(project::file)
        receiver.hasAllAccessTransformers tryAssign hasAllAccessTransformers
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ProguardConfigurableTask>())
}
