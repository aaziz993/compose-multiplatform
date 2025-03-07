package gradle.model.kotlin.kmp.jvm

import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmRunDsl

/**
 * Use [registerKotlinJvmRun] to create an instance
 */
@Serializable
internal data class KotlinJvmRunDsl(
    val mainClass: String? = null,
    val args: List<@Serializable(with = AnySerializer::class) Any>? = null,
    val classpath: List<String>? = null,
) {

    fun applyTo(run: KotlinJvmRunDsl) {
        run.mainClass tryAssign mainClass

        args?.let(run::setArgs)

        classpath?.let { classpath ->
            run.classpath(classpath)
        }
    }
}
