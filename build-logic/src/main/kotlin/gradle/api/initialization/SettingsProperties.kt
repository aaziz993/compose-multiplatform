package gradle.api.initialization

import gradle.api.Properties
import gradle.api.initialization.SettingsProperties.Companion.SETTINGS_PROPERTIES_EXT
import klib.data.type.primitives.string.scripting.ScriptConfig
import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import klib.data.type.serialization.serializers.any.SerializableAny

@Serializable
public data class SettingsProperties(
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
            settingsProperties =
                layout.settingsDirectory.file(SETTINGS_PROPERTIES_FILE).asFile(settings)
        }
    }
}

public var Settings.settingsProperties: SettingsProperties
    get() = extraProperties[SETTINGS_PROPERTIES_EXT] as SettingsProperties
    private set(value) {
        extraProperties[SETTINGS_PROPERTIES_EXT] = value
    }
