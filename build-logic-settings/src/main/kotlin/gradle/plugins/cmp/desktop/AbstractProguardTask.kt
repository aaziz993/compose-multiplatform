package gradle.plugins.cmp.desktop

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

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        inputFiles?.toTypedArray()?.let(recipient.inputFiles::from)
setInputFiles?.let(recipient.inputFiles::setFrom)
        recipient.mainJar tryAssign mainJar?.let(::file)
        configurationFiles?.toTypedArray()?.let(recipient.configurationFiles::from)
setConfigurationFiles?.let(recipient.configurationFiles::setFrom)
        recipient.dontobfuscate tryAssign dontobfuscate
        recipient.dontoptimize tryAssign dontoptimize
        recipient.joinOutputJars tryAssign joinOutputJars
        recipient.defaultComposeRulesFile tryAssign defaultComposeRulesFile?.let(::file)
        recipient.proguardVersion tryAssign proguardVersion
        proguardFiles?.toTypedArray()?.let(recipient.proguardFiles::from)
setProguardFiles?.let(recipient.proguardFiles::setFrom)
        recipient.javaHome tryAssign javaHome
        recipient.mainClass tryAssign mainClass
        recipient.maxHeapSize tryAssign maxHeapSize
        recipient.destinationDir tryAssign destinationDir?.let(layout.projectDirectory::dir)
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
    override val name: String = ""
) : AbstractProguardTask<org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask>())
}
