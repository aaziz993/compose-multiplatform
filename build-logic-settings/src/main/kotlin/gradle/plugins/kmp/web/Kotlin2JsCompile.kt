package gradle.plugins.kmp.web

import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tasks.ProducesKlib
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class Kotlin2JsCompile<T : org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>
    : KotlinJsCompile<T>, K2MultiplatformCompilationTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions>, ProducesKlib<T> {

    abstract val libraries: Set<String>?
    abstract val setLibraries: Set<String>?

    context(Project)
    override fun applyTo(recipient: T) {
        super<K2MultiplatformCompilationTask>.applyTo(recipient)
        super<ProducesKlib>.applyTo(recipient)

        libraries?.toTypedArray()?.let(recipient.libraries::from)
        setLibraries?.let(recipient.libraries::setFrom)
    }
}

@Serializable
@SerialName(value = "Kotlin2JsCompile")
internal data class Kotlin2JsCompileImpl(
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
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
) : Kotlin2JsCompile<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>())
}
