package gradle.api.initialization

import gradle.api.GradleScript
import gradle.api.initialization.file.CodeOfConductFile
import gradle.api.initialization.file.ContributingFile
import gradle.api.initialization.file.LicenseFile
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.initialization.file.ProjectFileImpl
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import java.util.*
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlinx.serialization.Serializable
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
    public val licenseFile: LicenseFile? = null,
    public val licenseHeaderFile: LicenseHeaderFile = LicenseHeaderFile(),
    public val codeOfConductFile: CodeOfConductFile? = null,
    public val contributingFile: ContributingFile? = null,
    public val files: List<ProjectFileImpl> = emptyList(),
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : GradleScript() {

    public companion object {

        context(settings: Settings)
        @Suppress("UnstableApiUsage")
        public operator fun invoke(): SettingsScript = with(settings) {
            layout.settingsDirectory.file(SETTINGS_PROPERTIES_FILE)
                .asFile<SettingsScript, Settings>(settings).also { properties ->
                    settingsScript = properties
                }.also(SettingsScript::invoke)
        }
    }
}

public var Settings.settingsScript: SettingsScript
    get() = extraProperties[SETTINGS_PROPERTIES_EXT] as SettingsScript
    private set(value) {
        extraProperties[SETTINGS_PROPERTIES_EXT] = value
    }
