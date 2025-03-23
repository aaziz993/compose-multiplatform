package gradle.plugins.kmp.jvm

import gradle.api.tryAssign
import gradle.collection.SerializableAnyList
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmRunDsl

/**
 * Use [registerKotlinJvmRun] to create an instance
 */
@Serializable
internal data class KotlinJvmRunDsl(
    val mainClass: String? = null,
    val args: SerializableAnyList? = null,
    val classpath: List<String>? = null,
) {

    fun applyTo(receiver: KotlinJvmRunDsl) {
        run.mainClass tryAssign mainClass

        args?.let(run::setArgs)

        classpath?.let { classpath ->
            run.classpath(classpath)
        }
    }
}
