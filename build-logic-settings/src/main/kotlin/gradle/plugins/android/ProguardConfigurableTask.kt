package gradle.plugins.android

import com.android.build.gradle.internal.tasks.ProguardConfigurableTask
import com.android.builder.core.ComponentTypeImpl
import gradle.api.tasks.applyTo

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import java.util.SortedSet
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
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
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
        testedMappingFile?.let(recipient.testedMappingFile::setFrom)
        classes?.let(recipient.classes::setFrom)
        recipient.resourcesJar tryAssign resourcesJar?.let(::file)
        referencedClasses?.let(recipient.referencedClasses::setFrom)
        referencedResources?.let(recipient.referencedResources::setFrom)
        recipient.extractedDefaultProguardFile tryAssign extractedDefaultProguardFile?.let(project.layout.projectDirectory::dir)
        generatedProguardFile?.let(recipient.generatedProguardFile::setFrom)
        configurationFiles?.let(recipient.configurationFiles::setFrom)
        libraryKeepRulesFileCollection?.let(recipient.libraryKeepRulesFileCollection::setFrom)
        recipient.ignoreFromInKeepRules tryAssign ignoreFromInKeepRules
        recipient.ignoreFromAllExternalDependenciesInKeepRules tryAssign ignoreFromAllExternalDependenciesInKeepRules
        recipient.mappingFile tryAssign mappingFile?.let(::file)
        recipient.hasAllAccessTransformers tryAssign hasAllAccessTransformers
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<ProguardConfigurableTask>())
}
