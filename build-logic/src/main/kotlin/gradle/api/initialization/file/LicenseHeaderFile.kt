package gradle.api.initialization.file

import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

public const val LICENSE_HEADER_FILE_NAME: String = "LICENSE_HEADER"

@Serializable
public data class LicenseHeaderFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.NEWER
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "licenses"

    @Suppress("UnstableApiUsage")
    context(settings: Settings)
    override fun sync() {
        val intoFile = settings.layout.settingsDirectory.file(into).asFile

        val previousLicenseText = intoFile.takeIf(File::exists)?.readText()

        super.sync()

        val licenseText = intoFile.readText()

        val create = previousLicenseText == null || previousLicenseText != licenseText

        val slashLicenseFile = settings.layout.settingsDirectory.file(into).asFile.resolve("SLASH_$LICENSE_HEADER_FILE_NAME")
        val hashLicenseFile = settings.layout.settingsDirectory.file(into).asFile.resolve("HASH_$LICENSE_HEADER_FILE_NAME")
        val tagLicenseFile = settings.layout.settingsDirectory.file(into).asFile.resolve("TAG_$LICENSE_HEADER_FILE_NAME")

        if (create || !slashLicenseFile.exists())
            slashLicenseFile.writeText(
                "/**\n${
                    licenseText.lines().joinToString("\n", " * ")
                }\n */",
            )

        if (create || !hashLicenseFile.exists())
            hashLicenseFile.writeText(
                licenseText.lines().joinToString("\n", " # "),
            )

        if (create || !tagLicenseFile.exists())
            tagLicenseFile.writeText("$<--\n$licenseText\n -->")
    }
}
