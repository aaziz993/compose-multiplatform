package gradle.plugins.kmp.jvm

import gradle.api.tryAssign
import gradle.collection.SerializableAnyList
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmRunDsl

/**
 * Use [registerKotlinJvmRun] to create an instance
 */
@Serializable
internal data class KotlinJvmRunDsl(
    val mainClass: String? = null,
    val args: SerializableAnyList? = null,
    val setArgs: SerializableAnyList? = null,
    val classpath: Set<String>? = null,
    val setClasspath: Set<String>? = null,
) {

    context(Project)
    fun applyTo(receiver: KotlinJvmRunDsl) {
        receiver.mainClass tryAssign mainClass
        args?.let(receiver::args)
        setArgs?.let(receiver::setArgs)
        classpath?.toTypedArray()?.let(receiver::classpath)
        setClasspath?.toTypedArray()?.let(project::files)?.let(receiver::setClasspath)
    }
}
