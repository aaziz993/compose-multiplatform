package gradle.plugins.kmp.jvm

import gradle.plugins.kotlin.KotlinExecution
import gradle.serialization.serializer.JsonPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTestRun

/**
 * A [KotlinExecution.ExecutionSource] that provides the [classpath] and [testClassesDirs] where JVM test classes can be found.
 */
@Serializable(with = JvmClasspathTestRunSourceSerializer::class)
internal interface JvmClasspathTestRunSource : KotlinExecution.ExecutionSource {

    context(Project)
    fun applyTo(recipient: KotlinJvmTestRun)
}

internal object JvmClasspathTestRunSourceSerializer : JsonPolymorphicSerializer<JvmClasspathTestRunSource>(
    JvmClasspathTestRunSource::class,
) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<JvmClasspathTestRunSource> =
        when {
            element.jsonObject.containsKey("compilation") -> SingleJvmCompilationTestRunSource.serializer()

            element.jsonObject.containsKey("classpath") &&
                element.jsonObject.containsKey("testClassesDirs") -> ClasspathOnlyTestRunSource.serializer()

            element.jsonObject.containsKey("classpathCompilations") &&
                element.jsonObject.containsKey("testCompilations") -> JvmCompilationsTestRunSource.serializer()

            else -> throw IllegalArgumentException("Unknown json value: $element")
        }
}
