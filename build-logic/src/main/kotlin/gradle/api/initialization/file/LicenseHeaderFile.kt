package gradle.api.initialization.file

import java.io.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

@Serializable
@SerialName("LicenseHeaderFile")
public data class LicenseHeaderFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.NEWER,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]",
    val licenseName: String? = null,
    val licenseNamePlaceholder: String = "[name of license]"
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = ".license.header"

    @Transient
    override val replace: Map<String, String> = listOfNotNull(
        year?.let { year -> yearPlaceholder to year },
        owner?.let { owner -> ownerPlaceholder to owner },
        licenseName?.let { name -> licenseNamePlaceholder to name },
    ).toMap()

    @Suppress("UnstableApiUsage")
    context(settings: Settings)
    override fun sync() {

        val intoFile = settings.layout.settingsDirectory.file(into).asFile

        val previousLicenseText = intoFile.takeIf(File::exists)?.readText()

        super.sync()

        val licenseText = intoFile.readText()

        val create = previousLicenseText == null || previousLicenseText != licenseText

        val slashLicenseFile = settings.layout.settingsDirectory.file("${into}.kt").asFile
        val hashLicenseFile = settings.layout.settingsDirectory.file("${into}.properties").asFile
        val tagLicenseFile = settings.layout.settingsDirectory.file("${into}.html").asFile

        if (create || !slashLicenseFile.exists())
            slashLicenseFile.writeText(
                "/**\n${
                    licenseText.lines().joinToString("\n") { line -> " * $line" }
                }\n */",
            )

        if (create || !hashLicenseFile.exists())
            hashLicenseFile.writeText(
                licenseText.lines().joinToString("\n") { line -> "# $line" },
            )

        if (create || !tagLicenseFile.exists())
            tagLicenseFile.writeText(
                "<!--\n${
                    licenseText.lines().joinToString("\n") { line -> " * $line" }
                }\n-->",
            )
    }
}
