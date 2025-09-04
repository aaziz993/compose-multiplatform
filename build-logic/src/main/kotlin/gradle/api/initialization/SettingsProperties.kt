package gradle.api.initialization

import gradle.api.Properties
import gradle.api.initialization.SettingsProperties.Companion.SETTINGS_PROPERTIES_EXT
import gradle.api.initialization.file.CodeOfConductFile
import gradle.api.initialization.file.ContributingFile
import gradle.api.initialization.file.LicenseFile
import gradle.api.initialization.file.LicenseHeaderFile
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import java.util.*

@Serializable
public data class SettingsProperties(
    val year: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
    val remote: MavenPomScm = MavenPomScm(),
    val developer: MavenPomDeveloper = MavenPomDeveloper(),
    val license: MavenPomLicense = MavenPomLicense(),
    val licenseFile: LicenseFile? = null,
    val licenseHeaderFile: LicenseHeaderFile? = null,
    val codeOfConductFile: CodeOfConductFile? = null,
    val contributingFile: ContributingFile? = null,
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>
) : Properties() {

    public companion object {

        internal const val LIBS_VERSION_CATALOG_EXT = "libs.versions.catalog.ext"

        internal const val LOCAL_PROPERTIES_EXT = "local.properties.ext"

        internal const val SETTINGS_PROPERTIES_EXT = "settings.properties.ext"

        private const val SETTINGS_PROPERTIES_FILE = "initialization.yaml"

        context(settings: Settings)
        @Suppress("UnstableApiUsage")
        internal operator fun invoke() = with(settings) {
            layout.settingsDirectory.file(SETTINGS_PROPERTIES_FILE)
                .asFile<SettingsProperties, Settings>(settings) { properties ->
                    settingsProperties = properties
                }
        }
    }
}

public var Settings.settingsProperties: SettingsProperties
    get() = extraProperties[SETTINGS_PROPERTIES_EXT] as SettingsProperties
    private set(value) {
        extraProperties[SETTINGS_PROPERTIES_EXT] = value
    }
