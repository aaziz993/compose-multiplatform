package gradle.model

import kotlinx.serialization.Serializable
import org.gradle.process.CommandLineArgumentProvider

@Serializable
internal data class CommandLineArgumentProvider(
    val arguments: List<String>? = null
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String>? = arguments
}
