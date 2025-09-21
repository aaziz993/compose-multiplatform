package gradle.api.initialization.file

import gradle.api.initialization.settingsScript
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

public const val LICENSE_HEADER_FILE: String = "licenses/LICENSE_HEADER"

@Serializable
public data class LicenseHeaderFile(
    val source: String = "licenses/LICENSE_HEADER",
    override val resolution: FileResolution = FileResolution.NEWER,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]",
    val name: String? = null,
    val namePlaceholder: String = "[name of license]"
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = LICENSE_HEADER_FILE

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    @Suppress("UnstableApiUsage")
    context(settings: Settings)
    override fun sync() {
        replace[yearPlaceholder] = year ?: settings.settingsScript.year
        replace[ownerPlaceholder] = owner ?: settings.settingsScript.developer.name!!
        replace[namePlaceholder] = name ?: settings.settingsScript.license.name!!

        val intoFile = settings.layout.settingsDirectory.file(into).asFile

        val previousLicenseText = intoFile.takeIf(File::exists)?.readText()

        super.sync()

        val licenseText = intoFile.readText()

        val create = previousLicenseText == null || previousLicenseText != licenseText

        val slashLicenseFile = settings.layout.settingsDirectory.file("${LICENSE_HEADER_FILE}_SLASH").asFile
        val hashLicenseFile = settings.layout.settingsDirectory.file("${LICENSE_HEADER_FILE}_HASH").asFile
        val tagLicenseFile = settings.layout.settingsDirectory.file("${LICENSE_HEADER_FILE}_TAG").asFile

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
                "$<--\n${
                    licenseText.lines().joinToString("\n") { line -> " * $line" }
                }\n -->",
            )
    }
}
