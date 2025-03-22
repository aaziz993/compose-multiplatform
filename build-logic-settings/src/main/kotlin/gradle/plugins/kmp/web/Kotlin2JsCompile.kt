package gradle.plugins.kmp.web


import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tasks.ProducesKlib
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

@Serializable
internal data class Kotlin2JsCompile(
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
    override val name: String? = null,,
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    val libraries: List<String>? = null,
) : KotlinJsCompile,
    K2MultiplatformCompilationTask,
    ProducesKlib {

        context(Project)
    override fun applyTo(recipient: T) {
        super<K2MultiplatformCompilationTask>._applyTo(named)
        super<ProducesKlib>._applyTo(named)

        named as Kotlin2JsCompile

        compilerOptions?._applyTo(named.compilerOptions)
        libraries?.toTypedArray()?.let(named.libraries::from)
setLibraries?.let(named.libraries::setFrom)
    }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<Kotlin2JsCompile>())
}
