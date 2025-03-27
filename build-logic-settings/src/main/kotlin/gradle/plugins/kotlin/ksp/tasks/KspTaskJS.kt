package gradle.plugins.kotlin.ksp.tasks

import com.google.devtools.ksp.gradle.KspTaskJS
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.SubpluginOption
import gradle.plugins.kotlin.targets.web.Kotlin2JsCompile
import gradle.plugins.kotlin.targets.web.KotlinJsCompilerOptions
import gradle.process.CommandLineArgumentProvider
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KspTaskJS(
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
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
    override val produceUnpackagedKlib: Boolean? = null,
    override val options: List<SubpluginOption>? = null,
    override val setOptions: List<SubpluginOption>? = null,
    override val commandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
    override val setCommandLineArgumentProviders: List<CommandLineArgumentProvider>? = null,
) : Kotlin2JsCompile<KspTaskJS>(), KspTask<KspTaskJS> {

    context(Project)
    override fun applyTo(receiver: KspTaskJS) {
        super<Kotlin2JsCompile>.applyTo(receiver)
        super<KspTask>.applyTo(receiver)
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KspTaskJS>())
}
