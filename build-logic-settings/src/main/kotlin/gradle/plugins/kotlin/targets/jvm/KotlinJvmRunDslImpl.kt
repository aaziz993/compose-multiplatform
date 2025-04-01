package gradle.plugins.kotlin.targets.jvm

import gradle.accessors.files
import gradle.api.getByNameOrAll
import gradle.api.provider.tryAssign
import gradle.collection.SerializableAnyList
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal interface KotlinJvmRunDsl {

    /**
     * ## See [JavaExec.mainClass]
     */
    val mainClass: String?

    /**
     * ## See [JavaExec.args]
     */
    val args: SerializableAnyList?

    /**
     * ## See [JavaExec.setArgs]
     */
    val setArgs: SerializableAnyList?

    /**
     * ## See [JavaExec.classpath]
     */
    val classpath: Set<String>?

    /**
     * ## See [JavaExec.setClasspath]
     */
    val setClasspath: Set<String>?

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmRunDsl, target: KotlinJvmTarget) {
        receiver.mainClass tryAssign mainClass
        args?.let(receiver::args)
        receiver::setArgs trySet setArgs
        classpath?.filter { !it.startsWith("$") }?.toTypedArray()?.let(receiver::classpath)
        receiver::setClasspath trySet setClasspath?.let(project::files)
        classpath?.mapNotNull {
            if (it.startsWith("\$compilation.")) it.substringAfter(".", "")
            else null
        }?.map(target.compilations::getByNameOrAll)?.toTypedArray()?.let(receiver::classpath)
    }
}

/**
 * Use [registerKotlinJvmRun] to create an instance
 */
@Serializable
internal data class KotlinJvmRunDslImpl(
    override val mainClass: String? = null,
    override val args: SerializableAnyList? = null,
    override val setArgs: SerializableAnyList? = null,
    override val classpath: Set<String>? = null,
    override val setClasspath: Set<String>? = null,
) : KotlinJvmRunDsl
