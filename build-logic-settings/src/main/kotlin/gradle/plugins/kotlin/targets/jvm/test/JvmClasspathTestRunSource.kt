package gradle.plugins.kotlin.targets.jvm.test

import gradle.plugins.kotlin.KotlinExecution
import klib.data.type.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * A [KotlinExecution.ExecutionSource] that provides the [classpath] and [testClassesDirs] where JVM test classes can be found.
 */
@Serializable(with = JvmClasspathTestRunSourceSerializer::class)
internal interface JvmClasspathTestRunSource : KotlinExecution.ExecutionSource

internal object JvmClasspathTestRunSourceSerializer : JsonContentPolymorphicSerializer<JvmClasspathTestRunSource>(
    JvmClasspathTestRunSource::class,
) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<JvmClasspathTestRunSource> =
        when {
            SingleJvmCompilationTestRunSource::compilation.name in element.jsonObject -> SingleJvmCompilationTestRunSource.serializer()

            ClasspathOnlyTestRunSource::classpath.name in element.jsonObject &&
                ClasspathOnlyTestRunSource::testClassesDirs.name in element.jsonObject -> ClasspathOnlyTestRunSource.serializer()

            JvmCompilationsTestRunSource::classpathCompilations.name in element.jsonObject &&
                JvmCompilationsTestRunSource::testCompilations.name in element.jsonObject -> JvmCompilationsTestRunSource.serializer()

            else -> throw IllegalArgumentException("Unknown json value: $element")
        }
}
