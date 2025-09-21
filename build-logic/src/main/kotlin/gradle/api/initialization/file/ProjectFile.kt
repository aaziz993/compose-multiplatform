package gradle.api.initialization.file

import arrow.core.fold
import java.io.File
import java.net.URI
import java.security.MessageDigest
import klib.data.type.isValidHttpUrl
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.gradle.internal.impldep.org.apache.ivy.util.url.ApacheURLLister

public abstract class ProjectFile {

    public abstract val from: List<String>
    public abstract val into: String
    public abstract val resolution: FileResolution
    public open val replace: Map<String, String> = emptyMap()

    context(settings: Settings)
    public open fun sync() {
        val (fromUrls, fromFiles) = from.partition(String::isValidHttpUrl)

        fromUrls.flatMap { url ->
            if (url.endsWith("/")) ApacheURLLister().listFiles(URI(url).toURL())
            else listOf(URI(url).toURL())
        }.forEach { fromUrl ->
            settings.trySync({ fromUrl.readText().replace() }) {
                fromUrl.openConnection().lastModified
            }
        }

        fromFiles.forEach { fromFile ->
            val file = File(fromFile)
            settings.trySync({ file.readText().replace() }) { file.lastModified() }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Settings.trySync(
        readText: () -> String,
        lastModified: () -> Long,
    ) {
        val file = settings.layout.settingsDirectory.file(into).asFile

        if (!file.exists()) {
            file.parentFile?.mkdirs()
            return file.writeText(readText())
        }

        when (resolution) {
            FileResolution.OVERRIDE -> file.writeText(readText())
            FileResolution.MODIFIED -> readText().let { text ->
                if (file.readText().hash() != text.hash()) file.writeText(text)
            }

            FileResolution.NEWER -> if (file.lastModified() < lastModified()) file.writeText(readText())

            else -> Unit
        }
    }

    private fun String.hash(algorithm: String = "SHA-256"): String =
        MessageDigest.getInstance(algorithm).digest(toByteArray())
            .joinToString("", transform = "%02x"::format)

    private fun String.replace() =
        replace.fold(this) { acc, (key, value) -> acc.replace(key, value) }
}

@Serializable
public data class ProjectFileImpl(
    override val from: List<String>,
    override val into: String,
    override val resolution: FileResolution = FileResolution.NEWER,
    override val replace: Map<String, String> = emptyMap()
) : ProjectFile()
