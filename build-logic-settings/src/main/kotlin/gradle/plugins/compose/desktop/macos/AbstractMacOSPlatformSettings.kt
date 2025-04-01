package gradle.plugins.compose.desktop.macos

import gradle.plugins.compose.desktop.platform.AbstractPlatformSettings
import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.jetbrains.compose.desktop.application.dsl.AbstractMacOSPlatformSettings

internal abstract class AbstractMacOSPlatformSettings<T : AbstractMacOSPlatformSettings> : AbstractPlatformSettings<T>() {

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
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::packageName trySet packageName
        receiver::packageBuildVersion trySet packageBuildVersion
        receiver::dmgPackageVersion trySet dmgPackageVersion
        receiver::dmgPackageBuildVersion trySet dmgPackageBuildVersion
        receiver::appCategory trySet appCategory
        receiver::minimumSystemVersion trySet minimumSystemVersion
        receiver::bundleID trySet bundleID
        signing?.applyTo(receiver.signing)
        notarization?.applyTo(receiver.notarization)
    }
}
