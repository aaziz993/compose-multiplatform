@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.nonamper.model

import java.nio.file.Path
import org.gradle.api.invocation.Gradle
import org.jetbrains.amper.core.messages.ProblemReporterContext
import org.jetbrains.amper.gradle.getOrNull
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.amper.core.Result

private const val KNOWN_MODEL_EXT = "org.jetbrains.non.amper.gradle.ext.model"

internal data class NonAmperModel(
    val
) {
    companion object {

        context(ProblemReporterContext)
        internal fun getGradleAmperModel(
            rootProjectDir: Path,
            subprojectDirs: List<Path>,
            loader: ClassLoader = Thread.currentThread().contextClassLoader,

            ): Result<NonAmperModule> {
        }
    }
}

internal var Gradle.knownNonAmperModel: NonAmperModel?
    get() = extraProperties.getOrNull<NonAmperModel>(KNOWN_MODEL_EXT)
    set(value) {
        extraProperties[KNOWN_MODEL_EXT] = value
    }
