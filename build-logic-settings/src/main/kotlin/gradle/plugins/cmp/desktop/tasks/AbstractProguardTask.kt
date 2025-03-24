package gradle.plugins.cmp.desktop.tasks

import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class AbstractProguardTask<T : org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>
    : AbstractComposeDesktopTask<T>() {

    abstract val inputFiles: Set<String>?
    abstract val mainJar: String?
    abstract val configurationFiles: LinkedHashSet<String>?
    abstract val dontobfuscate: Boolean?
    abstract val dontoptimize: Boolean?
    abstract val joinOutputJars: Boolean?

    // todo: DSL for excluding default rules
    // also consider pulling coroutines rules from coroutines artifact
    // https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/resources/META-INF/proguard/coroutines.pro
    abstract val defaultComposeRulesFile: String?
    abstract val proguardVersion: String?
    abstract val proguardFiles: LinkedHashSet<String>?
    abstract val javaHome: String?
    abstract val mainClass: String?
    abstract val maxHeapSize: String?
    abstract val destinationDir: String?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        inputFiles?.toTypedArray()?.let(receiver.inputFiles::from)
setInputFiles?.let(receiver.inputFiles::setFrom)
        receiver.mainJar tryAssign mainJar?.let(project::file)
        configurationFiles?.toTypedArray()?.let(receiver.configurationFiles::from)
setConfigurationFiles?.let(receiver.configurationFiles::setFrom)
        receiver.dontobfuscate tryAssign dontobfuscate
        receiver.dontoptimize tryAssign dontoptimize
        receiver.joinOutputJars tryAssign joinOutputJars
        receiver.defaultComposeRulesFile tryAssign defaultComposeRulesFile?.let(project::file)
        receiver.proguardVersion tryAssign proguardVersion
        proguardFiles?.toTypedArray()?.let(receiver.proguardFiles::from)
setProguardFiles?.let(receiver.proguardFiles::setFrom)
        receiver.javaHome tryAssign javaHome
        receiver.mainClass tryAssign mainClass
        receiver.maxHeapSize tryAssign maxHeapSize
        receiver.destinationDir tryAssign destinationDir?.let(project.layout.projectDirectory::dir)
    }
}


@Serializable
@SerialName("AbstractProguardTask")
internal data class AbstractProguardTaskImpl(
    override val inputFiles: Set<String>? = null,
    override val mainJar: String? = null,
    override val configurationFiles: LinkedHashSet<String>? = null,
    override val dontobfuscate: Boolean? = null,
    override val dontoptimize: Boolean? = null,
    override val joinOutputJars: Boolean? = null,
    override val defaultComposeRulesFile: String? = null,
    override val proguardVersion: String? = null,
    override val proguardFiles: LinkedHashSet<String>? = null,
    override val javaHome: String? = null,
    override val mainClass: String? = null,
    override val maxHeapSize: String? = null,
    override val destinationDir: String? = null,
    override val verbose: Boolean? = null,
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
) : AbstractProguardTask<org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>())
}
