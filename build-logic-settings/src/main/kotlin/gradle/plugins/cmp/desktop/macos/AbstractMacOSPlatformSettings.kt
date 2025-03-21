package gradle.plugins.cmp.desktop.macos

import gradle.api.trySet
import gradle.plugins.cmp.desktop.platform.AbstractPlatformSettings
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractMacOSPlatformSettings

internal abstract class AbstractMacOSPlatformSettings<T: AbstractMacOSPlatformSettings> : AbstractPlatformSettings<T>() {

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
    open fun applyTo(recipient: T) {
        super.applyTo(recipient)

        recipient::packageName trySet packageName
        recipient::packageBuildVersion trySet packageBuildVersion
        recipient::dmgPackageVersion trySet dmgPackageVersion
        recipient::dmgPackageBuildVersion trySet dmgPackageBuildVersion
        recipient::appCategory trySet appCategory
        recipient::minimumSystemVersion trySet minimumSystemVersion
        recipient::bundleID trySet bundleID
        signing?.applyTo(recipient.signing)
        notarization?.applyTo(recipient.notarization)
    }
}
