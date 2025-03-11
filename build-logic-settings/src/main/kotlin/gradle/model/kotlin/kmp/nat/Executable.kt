package gradle.model.kotlin.kmp.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable

internal abstract class Executable : NativeBinary {

    /**
     * The fully qualified name of the main function. For an example:
     *
     * - "main"
     * - "com.example.main"
     *
     *  The main function can either take no arguments or an Array<String>.
     */
    abstract val entryPoint: String?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as Executable

        entryPoint?.let(named::entryPoint)
    }
}
