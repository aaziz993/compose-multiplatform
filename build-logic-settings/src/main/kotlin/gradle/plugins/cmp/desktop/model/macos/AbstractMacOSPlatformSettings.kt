package gradle.plugins.cmp.desktop.model.macos

import gradle.plugins.cmp.desktop.model.AbstractPlatformSettings
import gradle.api.trySet
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractMacOSPlatformSettings

internal abstract class AbstractMacOSPlatformSettings : AbstractPlatformSettings() {

    abstract val packageName: String?

    abstract val packageBuildVersion: String?
    abstract val dmgPackageVersion: String?
    abstract val dmgPackageBuildVersion: String?
    abstract val appCategory: String?
    abstract val minimumSystemVersion: String?

    /**
     * An application's unique identifier across Apple's ecosystem.
     *
     * May only contain alphanumeric characters (A-Z,a-z,0-9), hyphen (-) and period (.) characters
     *
     * Use of a reverse DNS notation (e.g. com.mycompany.myapp) is recommended.
     */
    abstract val bundleID: String?

    abstract val signing: MacOSSigningSettings?

    abstract val notarization: MacOSNotarizationSettings?

    context(Project)
    fun applyTo(settings: AbstractMacOSPlatformSettings) {
        super.applyTo(settings)

        settings::packageName trySet packageName
        settings::packageBuildVersion trySet packageBuildVersion
        settings::dmgPackageVersion trySet dmgPackageVersion
        settings::dmgPackageBuildVersion trySet dmgPackageBuildVersion
        settings::appCategory trySet appCategory
        settings::minimumSystemVersion trySet minimumSystemVersion
        settings::bundleID trySet bundleID
        signing?.applyTo(settings.signing)
        notarization?.applyTo(settings.notarization)
    }
}
