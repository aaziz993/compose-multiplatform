package gradle.api.initialization

import gradle.api.GradleScript
import gradle.api.initialization.file.CodeOfConductFile
import gradle.api.initialization.file.ContributingFile
import gradle.api.initialization.file.LicenseFile
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.initialization.file.ProjectFile
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import java.io.File
import java.util.Calendar
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public const val SETTINGS_PROPERTIES_EXT: String = "settings.properties.ext"

public const val SETTINGS_PROPERTIES_FILE: String = "initialization.yaml"

@Serializable
public class SettingsScript(
    public val year: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
    public val remote: MavenPomScm = MavenPomScm(),
    public val developer: MavenPomDeveloper = MavenPomDeveloper(),
    public val license: MavenPomLicense = MavenPomLicense(),
    @SerialName("licenseFile")
    private val _licenseFile: LicenseFile? = null,
    public val licenseHeaderFile: LicenseHeaderFile? = null,
    public val codeOfConductFile: CodeOfConductFile? = null,
    public val contributingFile: ContributingFile? = null,
    public val files: List<ProjectFile> = emptyList(),
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : GradleScript() {

    @Transient
    public val licenseFile: LicenseFile = _licenseFile?.copy(license.url!!) ?: LicenseFile(license.url!!)

    public companion object {

        context(settings: Settings)
        @Suppress("UnstableApiUsage")
        public operator fun invoke(): Unit = with(settings) {
            layout.settingsDirectory.file(SETTINGS_PROPERTIES_FILE)
                .asFile.takeIf(File::exists)?.invoke<SettingsScript, Settings>(settings)
                .let { properties ->
                    settingsScript = properties ?: SettingsScript(script = emptyList(), fileTree = emptyMap())
                    if (properties != null) properties()
                }
        }
    }
}

public var Settings.settingsScript: SettingsScript
    get() = extraProperties[SETTINGS_PROPERTIES_EXT] as SettingsScript
    private set(value) {
        extraProperties[SETTINGS_PROPERTIES_EXT] = value
    }
