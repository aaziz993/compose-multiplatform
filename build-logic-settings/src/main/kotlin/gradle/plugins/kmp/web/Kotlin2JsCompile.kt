package gradle.plugins.kmp.web

import gradle.collection.SerializableAnyMap
import gradle.tasks.K2MultiplatformCompilationTask
import gradle.tasks.ProducesKlib
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

@Serializable
internal data class Kotlin2JsCompile(
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
    override val produceUnpackagedKlib: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    val libraries: List<String>? = null,
) : KotlinJsCompile,
    K2MultiplatformCompilationTask,
    ProducesKlib {

    context(Project)
    override fun applyTo(named: Named) {
        super<K2MultiplatformCompilationTask>.applyTo(named)
        super<ProducesKlib>.applyTo(named)

        named as Kotlin2JsCompile

        compilerOptions?.applyTo(named.compilerOptions)
        libraries?.let(named.libraries::setFrom)
    }

    context(Project)
    override fun applyTo() = applyTo(tasks.withType<Kotlin2JsCompile>())
}
