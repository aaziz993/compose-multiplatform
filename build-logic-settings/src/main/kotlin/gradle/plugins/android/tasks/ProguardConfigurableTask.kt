package gradle.plugins.android.tasks

import com.android.build.gradle.internal.tasks.ProguardConfigurableTask
import com.android.builder.core.ComponentTypeImpl
import gradle.api.tasks.applyTo
import gradle.api.tryAddAll
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
        receiver.testedMappingFile tryFrom testedMappingFile
        receiver.testedMappingFile trySetFrom setTestedMappingFile
        receiver.classes tryFrom classes
        receiver.classes trySetFrom setClasses
        receiver.resourcesJar tryAssign resourcesJar?.let(project::file)
        receiver.referencedClasses tryFrom referencedClasses
        receiver.referencedClasses trySetFrom setReferencedClasses
        receiver.referencedResources tryFrom referencedResources
        receiver.referencedResources trySetFrom setReferencedResources
        receiver.extractedDefaultProguardFile tryAssign extractedDefaultProguardFile?.let(project.layout.projectDirectory::dir)
        receiver.generatedProguardFile tryFrom generatedProguardFile
        receiver.generatedProguardFile trySetFrom setGeneratedProguardFile
        receiver.configurationFiles tryFrom configurationFiles
        receiver.configurationFiles trySetFrom setConfigurationFiles
        receiver.libraryKeepRulesFileCollection tryFrom libraryKeepRulesFileCollection
        receiver.libraryKeepRulesFileCollection trySetFrom setLibraryKeepRulesFileCollection
        receiver.ignoreFromInKeepRules tryAddAll ignoreFromInKeepRules
        receiver.ignoreFromInKeepRules tryAssign setIgnoreFromInKeepRules
        receiver.ignoreFromAllExternalDependenciesInKeepRules tryAssign ignoreFromAllExternalDependenciesInKeepRules
        receiver.mappingFile tryAssign mappingFile?.let(project::file)
        receiver.hasAllAccessTransformers tryAssign hasAllAccessTransformers
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<ProguardConfigurableTask>())
}
