package gradle.plugins.buildconfig

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.applyTo
import gradle.api.tryAssign
import gradle.plugins.buildconfig.generator.BuildConfigGenerator
import gradle.plugins.buildconfig.generator.BuildConfigJavaGenerator
import gradle.plugins.buildconfig.generator.BuildConfigKotlinGenerator
import gradle.plugins.buildconfig.tasks.BuildConfigTask
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = BuildConfigSourceSetObjectTransformingSerializer::class)
internal data class BuildConfigSourceSet(
    override val className: String? = null,
    override val packageName: String? = null,
    override val buildConfigFields: LinkedHashSet<BuildConfigField>? = null,
    override val name: String? = null,
    val generator: BuildConfigGenerator<*>? = null,
    val generateTask: BuildConfigTask? = null,
    val useJavaOutput: BuildConfigJavaGenerator? = null,
    val useKotlinOutput: BuildConfigKotlinGenerator? = null,
    /**
     * Creates a secondary build class with the given [className] in the same package
     */
    val forClass: Set<ForClass>? = null,
) : BuildConfigClassSpec<com.github.gmazzo.gradle.plugins.BuildConfigSourceSet> {

    context(Project)
    override fun applyTo(receiver: com.github.gmazzo.gradle.plugins.BuildConfigSourceSet) {
        super.applyTo(receiver)
        receiver.className tryAssign className
        receiver.packageName tryAssign packageName

        buildConfigFields?.forEach { buildConfigField ->
            buildConfigField.applyTo(receiver.buildConfigFields)
        }

        receiver.generator tryAssign generator?.toBuildConfigGenerator()

        useJavaOutput?.let { useJavaOutput ->
            receiver.useJavaOutput(useJavaOutput::applyTo)
        }

        useKotlinOutput?.let { useKotlinOutput ->
            receiver.useKotlinOutput(useKotlinOutput::applyTo)
        }

        forClass?.forEach { forClass ->
            receiver.forClass(forClass.packageName, forClass.className) {
                forClass.configureAction?.applyTo(this)
            }
        }
    }
}

private object BuildConfigSourceSetObjectTransformingSerializer
    : NamedObjectTransformingSerializer<BuildConfigSourceSet>(BuildConfigSourceSet.generatedSerializer())
