package gradle.plugins.cmp.desktop

import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractProguardTask : AbstractComposeDesktopTask() {

    abstract val inputFiles: List<String>?
    abstract val mainJar: String?
    abstract val configurationFiles: List<String>?
    abstract val dontobfuscate: Boolean?
    abstract val dontoptimize: Boolean?
    abstract val joinOutputJars: Boolean?

    // todo: DSL for excluding default rules
    // also consider pulling coroutines rules from coroutines artifact
    // https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/resources/META-INF/proguard/coroutines.pro
    abstract val defaultComposeRulesFile: String?
    abstract val proguardVersion: String?
    abstract val proguardFiles: List<String>?
    abstract val javaHome: String?
    abstract val mainClass: String?
    abstract val maxHeapSize: String?
    abstract val destinationDir: String?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask

        inputFiles?.let(named.inputFiles::setFrom)
        named.mainJar tryAssign mainJar?.let(::file)
        configurationFiles?.let(named.configurationFiles::setFrom)
        named.dontobfuscate tryAssign dontobfuscate
        named.dontoptimize tryAssign dontoptimize
        named.joinOutputJars tryAssign joinOutputJars
        named.defaultComposeRulesFile tryAssign defaultComposeRulesFile?.let(::file)
        named.proguardVersion tryAssign proguardVersion
        proguardFiles?.let(named.proguardFiles::setFrom)
        named.javaHome tryAssign javaHome
        named.mainClass tryAssign mainClass
        named.maxHeapSize tryAssign maxHeapSize
        named.destinationDir tryAssign destinationDir?.let(layout.projectDirectory::dir)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>())
}

@Serializable
@SerialName("AbstractProguardTask")
internal data class AbstractProguardTaskImpl(
    override val inputFiles: List<String>? = null,
    override val mainJar: String? = null,
    override val configurationFiles: List<String>? = null,
    override val dontobfuscate: Boolean? = null,
    override val dontoptimize: Boolean? = null,
    override val joinOutputJars: Boolean? = null,
    override val defaultComposeRulesFile: String? = null,
    override val proguardVersion: String? = null,
    override val proguardFiles: List<String>? = null,
    override val javaHome: String? = null,
    override val mainClass: String? = null,
    override val maxHeapSize: String? = null,
    override val destinationDir: String? = null,
    override val verbose: Boolean? = null,
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
    override val name: String = ""
) : AbstractProguardTask()
