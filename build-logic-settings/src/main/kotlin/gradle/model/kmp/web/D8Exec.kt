package gradle.model.kmp.web

import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import gradle.model.AbstractExecTask

@Serializable
internal data class D8Exec(
    override val commandLineArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val setCommandLineArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val args: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val setArgs: List<@Serializable(with = AnySerializer::class) Any>? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val setEnvironment: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val ignoreExitValue: Boolean? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    val d8Args: List<String>? = null,
    val inputFileProperty: String? = null,
) : AbstractExecTask<D8Exec>() {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.targets.js.d8.D8Exec

        named::d8Args trySet d8Args?.toMutableList()

        named.inputFileProperty tryAssign inputFileProperty?.let(::file)
    }
}
