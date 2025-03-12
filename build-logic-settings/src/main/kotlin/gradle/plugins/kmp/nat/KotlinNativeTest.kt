package gradle.plugins.kmp.nat

import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.KotlinTest
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

@Serializable
internal data class KotlinNativeTest(
    val executables: List<String>? = null,
    val args: List<String>? = null,
    val workingDir: String? = null,
    val environment: SerializableAnyMap? = null,
    val trackEnvironments: List<@Serializable(with = TrackEnvironmentTransformingSerializer::class) TrackEnvironment>? = null,
) : KotlinTest() {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinNativeTest

        named.executableProperty tryAssign executables?.let { files(*it.toTypedArray()) }
        named::args trySet args
        named::workingDir trySet workingDir

        environment?.let { environment ->
            named.environment = environment
        }

        trackEnvironments?.forEach { (name, tracked) ->
            named.trackEnvironment(name, tracked)
        }
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<KotlinNativeTest>())
}
